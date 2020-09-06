package com.tim32.emarket.shop;

import android.annotation.SuppressLint;
import android.widget.GridView;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.ShopRestClient;
import com.tim32.emarket.apiclients.clients.ShopSecureRestClient;
import com.tim32.emarket.apiclients.config.AuthStore;
import com.tim32.emarket.apiclients.dto.Shop;
import com.tim32.emarket.customcomponents.shop.ShopListAdapter;
import com.tim32.emarket.data.entity.Favorite;
import com.tim32.emarket.service.FavoritesService;
import com.tim32.emarket.service.ImageService;
import com.tim32.emarket.util.FragmentUtil;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;


@EFragment(R.layout.fragment_subscriptions_and_favorites)
public class SubscriptionsAndFavoritesFragment extends Fragment {

    private static final String TAG = SubscriptionsAndFavoritesFragment.class.getName();

    @ViewById(R.id.galleryGridView)
    GridView gridView;

    @RestService
    ShopSecureRestClient shopSecureRestClient;

    @Bean
    FavoritesService favoritesService;

    @Bean
    AuthStore authStore;

    @Bean
    ImageService imageService;

    ShopListAdapter shopListAdapter;

    @RestService
    ShopRestClient shopRestClient;

    @AfterViews
    void afterViews() {
        getActivity().setTitle(R.string.subscriptions_and_favorites);
        shopListAdapter = new ShopListAdapter(getContext(), imageService);
        gridView.setAdapter(shopListAdapter);
        loadShops();
    }

    @SuppressLint("NewApi")
    @Background
    void loadShops() {
        try {
            List<Favorite> favorites = favoritesService.findAll().stream().filter(favorite -> !favorite.isDeleted()).collect(Collectors.toList());
            Set<Long> shopIds = getShopIds(favorites);
            setShops(shopRestClient.findByIds(getCommaSeparatedShopIds(shopIds)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    private String getCommaSeparatedShopIds(Set<Long> shopIds) {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Long shopId : shopIds) {
            stringJoiner.add(shopId.toString());
        }
        return stringJoiner.toString();
    }

    private Set<Long> getShopIds(List<Favorite> favorites) {
        Set<Long> shopIds = new HashSet<>();
        for (Favorite favorite : favorites) {
            shopIds.add(favorite.getShopId());
        }
        return shopIds;
    }

    @UiThread
    void setShops(List<Shop> shops) {
        shopListAdapter.setItems(shops);
    }

    @ItemClick(R.id.galleryGridView)
    void listViewItemClick(Shop shop) {
        FragmentUtil.setRootFragment(this, ShopDetailFragment_.builder().arg(ShopDetailFragment.SHOP_ID, shop.getId()).build());
    }
}
