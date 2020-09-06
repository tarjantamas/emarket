package com.tim32.emarket.shop;

import android.util.Log;
import android.widget.GridView;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.ShopSecureRestClient;
import com.tim32.emarket.apiclients.config.AuthStore;
import com.tim32.emarket.apiclients.dto.Shop;
import com.tim32.emarket.customcomponents.shop.ShopListAdapter;
import com.tim32.emarket.service.ImageService;
import com.tim32.emarket.util.FragmentUtil;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

@EFragment(R.layout.fragment_shops_browser)
public class ShopsBrowserByCompanyFragment extends Fragment {

    private static final String TAG = SubscriptionsAndFavoritesFragment.class.getName();

    public static String COMPANY_ID = "company_id";

    @ViewById(R.id.galleryGridView)
    GridView gridView;

    @RestService
    ShopSecureRestClient shopSecureRestClient;

    @Bean
    AuthStore authStore;

    @Bean
    ImageService imageService;

    ShopListAdapter shopListAdapter;

    @AfterViews
    void afterViews() {
        getActivity().setTitle(R.string.shops_browser_by_company_title);
        shopListAdapter = new ShopListAdapter(getContext(), imageService);
        gridView.setAdapter(shopListAdapter);
        loadShops();
    }

    @Background
    void loadShops() {
        try {
            List<Shop> shops = shopSecureRestClient.findByCompanyId(getArguments().getLong(COMPANY_ID));
            setShops(shops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void setShops(List<Shop> shops) {
        shopListAdapter.setItems(shops);
    }

    @ItemClick(R.id.galleryGridView)
    void listViewItemClick(Shop shop) {
        Log.i(TAG, shop.getName() + " " + shop.getDescription());
        FragmentUtil.setRootFragment(this, ShopDetailFragment_.builder().arg(ShopDetailFragment.SHOP_ID, shop.getId()).build());
    }
}
