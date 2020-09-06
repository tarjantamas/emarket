package com.tim32.emarket.shop;

import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.ShopRestClient;
import com.tim32.emarket.apiclients.clients.ShopSecureRestClient;
import com.tim32.emarket.apiclients.config.GlideApp;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.Shop;
import com.tim32.emarket.product.ProductBrowserFragment_;
import com.tim32.emarket.service.AuthService;
import com.tim32.emarket.service.FavoritesService;
import com.tim32.emarket.service.SyncObserver;
import com.tim32.emarket.util.BundleUtil;
import com.tim32.emarket.util.FragmentUtil;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;


@EFragment(R.layout.fragment_shop_detail)
public class ShopDetailFragment extends Fragment implements SyncObserver {

    public static String SHOP_ID = "shop_id";

    @ViewById(R.id.headerImage)
    RoundedImageView headerImage;

    @ViewById(R.id.shopName)
    TextView shopName;

    @ViewById(R.id.shopDescription)
    TextView shopDescription;

    @ViewById(R.id.productListButton)
    Button productListButton;

    @ViewById(R.id.galleryGridView)
    GridView galleryGridView;

    @ViewById(R.id.gridView)
    GridView gridView;

    @ViewById(R.id.favoriteButton)
    FloatingActionButton favoriteButton;

    @ViewById(R.id.viewPager)
    ViewPager viewPager;

    @Bean
    FavoritesService favoritesService;

    @ViewById(R.id.tabLayout)
    TabLayout tabLayout;

    @RestService
    ShopRestClient shopRestClient;

    @RestService
    ShopSecureRestClient shopSecureRestClient;

    @Bean
    AuthService authService;

    private Shop shop;

    @AfterViews
    void afterViews() {
        getActivity().setTitle(R.string.shop_detail_fragment_title);
        loadShop();
        if (authService.isLoggedIn()) {
            checkIfFavorite();
        } else {
            favoriteButton.hide();
        }

        favoritesService.subscribe(this);
    }

    @Click(R.id.productListButton)
    void productListButtonClicked() {
        if (shop == null) {
            return;
        }
        FragmentUtil.setRootFragment(this, ProductBrowserFragment_.builder()
                .arg(SHOP_ID, shop.getId())
                .build());
    }

    @Background
    @Click(R.id.favoriteButton)
    void favoriteButtonClicked() {
        try {
            if (!favoritesService.isFavorite(this.getArguments().getLong(SHOP_ID))) {
                favoritesService.add(this.getArguments().getLong(SHOP_ID));
                favoritedToast();
            } else {
                favoritesService.remove(this.getArguments().getLong(SHOP_ID));
                unfavoritedToast();
            }
        } catch (Exception e) {
            FragmentUtil.performBackAction(getActivity());
            unsuccessfulToast();
        }
    }


    @UiThread
    void unfavoritedToast() {
        Toast.makeText(getContext(), "Shop removed from favorites!", Toast.LENGTH_SHORT).show();
        if (favoriteButton == null) {
            return;
        }
        favoriteButton.setImageResource(R.drawable.favorite_grey);
    }

    @UiThread
    void unsuccessfulToast() {
        Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void favoritedToast() {
        Toast.makeText(getContext(), "Shop favorited!", Toast.LENGTH_SHORT).show();
        if (favoriteButton == null) {
            return;
        }
        favoriteButton.setImageResource(R.drawable.favorite);
    }

    @Background
    void checkIfFavorite() {
        Long shopId = BundleUtil.getLongOrNull(getArguments(), SHOP_ID);
        if (shopId == null) {
            return;
        }
        if (favoritesService.isFavorite(shopId)) {
            setFavoriteIcon();
        } else {
            setUnfavoriteIcon();
        }
    }

    @UiThread
    void setFavoriteIcon() {
        if (favoriteButton == null) {
            return;
        }
        favoriteButton.setImageResource(R.drawable.favorite);
    }

    @UiThread
    void setUnfavoriteIcon() {
        if (favoriteButton == null) {
            return;
        }
        favoriteButton.setImageResource(R.drawable.favorite_grey);
    }

    @Background
    void loadShop() {
        try {
            Long shopId = BundleUtil.getLongOrNull(getArguments(), SHOP_ID);
            if (shopId == null) {
                loadShopFailed();
                FragmentUtil.performBackAction(getActivity());
                return;
            }
            setShop(shopRestClient.findById(shopId));
            loadHeaderImage(shopId);
        } catch (Exception e) {
            loadShopFailed();
            FragmentUtil.performBackAction(getActivity());
        }
    }

    @UiThread
    void loadHeaderImage(long shopId) {
        GlideApp.with(this).load(RestApiUrl.shopImage + "/" + shopId).error(R.drawable.placeholder).into(headerImage);
    }

    @UiThread
    void loadShopFailed() {
        Toast.makeText(getContext(), "Unable to load shop details", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void setShop(Shop shop) {
        this.shop = shop;
        shopName.setText(shop.getName());
        shopDescription.setText(shop.getDescription());
        initViewPager(shop);
    }

    private void initViewPager(Shop shop) {
        viewPager.setAdapter(new ShopDetailTabPagerAdapter(getChildFragmentManager(), FragmentPagerAdapter.POSITION_NONE, shop));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSync() {
        checkIfFavorite();
    }

    @Override
    public void onDestroy() {
        favoritesService.unsubscribe(this);
        super.onDestroy();
    }
}
