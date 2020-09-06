package com.tim32.emarket.shop;

import android.widget.GridView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.ImageListResponse;
import com.tim32.emarket.customcomponents.gallery.GalleryAdapter;
import com.tim32.emarket.service.ImageService;
import com.tim32.emarket.util.BundleUtil;
import org.androidannotations.annotations.*;

import java.util.ArrayList;
import java.util.stream.Collectors;


@EFragment(R.layout.fragment_shop_detail_gallery_tab)
public class ShopDetailGalleryTabFragment extends Fragment {

    public static final String SHOP_ID = "shop_id";

    @ViewById(R.id.gridView)
    GridView gridView;

    @ViewById(R.id.viewPager)
    ViewPager viewPager;

    @Bean
    ImageService imageService;

    private GalleryAdapter galleryAdapter;

    @AfterViews
    void afterViews() {
        galleryAdapter = new GalleryAdapter(getContext(), new ArrayList<>());
        gridView.setAdapter(galleryAdapter);
        gridView.setNestedScrollingEnabled(true);
        loadImages();
    }

    @Background
    void loadImages() {
        Long shopId = BundleUtil.getLongOrNull(getArguments(), SHOP_ID);
        if (shopId == null) {
            return;
        }
        try {
            ImageListResponse images = imageService.getShopImages(shopId);
            setImages(images);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    @SuppressWarnings("NewApi")
    void setImages(ImageListResponse images) {
        galleryAdapter.setImageUrls(images.stream().map(imageResponse -> RestApiUrl.root + RestApiUrl.image + imageResponse.getId())
                .collect(Collectors.toList()));
    }
}
