package com.tim32.emarket.shop;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SearchView;
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
public class ShopsBrowserFragment extends Fragment {

    private static final String TAG = SubscriptionsAndFavoritesFragment.class.getName();

    @RestService
    ShopSecureRestClient shopSecureRestClient;

    @Bean
    AuthStore authStore;

    @ViewById(R.id.galleryGridView)
    GridView gridView;

    ShopListAdapter shopListAdapter;

    ImageView profileImage;

    @Bean
    ImageService imageService;

    @AfterViews
    void afterViews() {
        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.shop_browser_fragment_title);
        shopListAdapter = new ShopListAdapter(getContext(), imageService);
        gridView.setAdapter(shopListAdapter);
        loadShops();
    }

    @Background
    void loadShops() {
        try {
            List<Shop> shops = shopSecureRestClient.findAll();
            setShops(shops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setProfilePicture(byte[] imageResponse) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        Bitmap bmp = BitmapFactory.decodeByteArray(imageResponse, 0, imageResponse.length, options);
        profileImage.setImageBitmap(bmp);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.actionbar_menu, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                shopListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }
}
