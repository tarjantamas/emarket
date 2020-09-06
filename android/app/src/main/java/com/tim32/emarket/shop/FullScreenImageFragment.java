package com.tim32.emarket.shop;

import android.widget.GridView;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.ShopRestClient;
import com.tim32.emarket.apiclients.clients.ShopSecureRestClient;
import com.tim32.emarket.apiclients.config.GlideApp;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

@EFragment(R.layout.fragment_fullscreen_image)
public class FullScreenImageFragment extends Fragment {


    @ViewById(R.id.headerImage)
    RoundedImageView headerImage;

    @ViewById(R.id.gridView)
    GridView gridView;

    @ViewById(R.id.tabLayout)
    TabLayout tabLayout;

    @RestService
    ShopRestClient shopRestClient;

    @RestService
    ShopSecureRestClient shopSecureRestClient;

    String[] images = new String[150];

    int position;

    @AfterViews
    void afterViews() {
        getActivity().setTitle(R.string.shop_detail_fragment_title);
        position = getArguments().getInt("position");
        images = getArguments().getStringArray("images");
        final String shopHeaderMockImageUrl = images[position];
        GlideApp.with(this).load(shopHeaderMockImageUrl).into(headerImage);

    }

    @Click(R.id.headerImage)
    void imageClick() {
        final String shopHeaderMockImageUrl = images[position + 1];
        position += 1;
        GlideApp.with(this).load(shopHeaderMockImageUrl).into(headerImage);
    }
}
