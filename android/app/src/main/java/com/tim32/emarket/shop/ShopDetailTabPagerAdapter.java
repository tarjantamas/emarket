package com.tim32.emarket.shop;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.tim32.emarket.apiclients.dto.Shop;
import com.tim32.emarket.customcomponents.map.GoogleMapsFragment;
import com.tim32.emarket.customcomponents.map.GoogleMapsFragment_;

public class ShopDetailTabPagerAdapter extends FragmentPagerAdapter {

    public static final String TAG = ShopDetailFragment.class.getName();

    private final Shop shop;

    public ShopDetailTabPagerAdapter(@NonNull FragmentManager fm, int behavior, Shop shop) {
        super(fm, behavior);
        this.shop = shop;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return GoogleMapsFragment_.builder()
                        .arg(GoogleMapsFragment.LONGITUDE, shop.getLongitude())
                        .arg(GoogleMapsFragment.LATITUDE, shop.getLatitude())
                        .arg(GoogleMapsFragment.MARKER_TITLE, shop.getName())
                        .build();
            case 2:
                return ShopDetailCompanyTabFragment_.builder()
                        .arg(ShopDetailCompanyTabFragment.COMPANY_ID, shop.getCompanyId())
                        .build();
            default:
                return ShopDetailGalleryTabFragment_.builder().arg(ShopDetailGalleryTabFragment.SHOP_ID, shop.getId()).build();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Gallery";
            case 1:
                return "Location";
            case 2:
                return "Company";
        }
        Log.e(TAG, "Invalid position");
        return "";
    }
}
