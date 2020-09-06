package com.tim32.emarket.product;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.CompanyRestClient;
import com.tim32.emarket.apiclients.clients.ProductRestClient;
import com.tim32.emarket.apiclients.clients.ShopRestClient;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.apiclients.dto.Company;
import com.tim32.emarket.apiclients.dto.ImageListResponse;
import com.tim32.emarket.apiclients.dto.Product;
import com.tim32.emarket.apiclients.dto.Shop;
import com.tim32.emarket.customcomponents.gallery.ImageSliderAdapter;
import com.tim32.emarket.customcomponents.shop.ShopListAdapter;
import com.tim32.emarket.service.AuthService;
import com.tim32.emarket.service.ImageService;
import com.tim32.emarket.shop.ShopDetailFragment;
import com.tim32.emarket.shop.ShopDetailFragment_;
import com.tim32.emarket.util.*;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;
import java.util.stream.Collectors;

@EFragment(R.layout.fragment_product_detail)
public class ProductDetailFragment extends Fragment {

    public static final String PRODUCT_ID = "product_id";

    public static final String TAG = ProductDetailFragment.class.getName();

    @ViewById(R.id.name)
    TextView name;

    @ViewById(R.id.description)
    TextView description;

    @ViewById(R.id.price)
    TextView price;

    @ViewById(R.id.shopList)
    GridView shopList;

    @ViewById(R.id.imageSlider)
    SliderView imageSlider;

    @ViewById(R.id.editProductButton)
    MaterialButton editProductButton;

    @RestService
    ShopRestClient shopRestClient;

    @RestService
    CompanyRestClient companyRestClient;

    @RestService
    ProductRestClient productRestClient;

    @Bean
    AuthService authService;

    @Bean
    ImageService imageService;

    private Product product;

    private List<Shop> shops;

    private ShopListAdapter shopListAdapter;

    private ImageSliderAdapter imageSliderAdapter;

    @SuppressLint("SetTextI18n")
    @AfterViews
    void afterViews() {
        editProductButton.setVisibility(View.GONE);
        String productId = BundleUtil.getStringOrNull(getArguments(), PRODUCT_ID);
        if (productId == null) {
            return;
        }
        loadProduct(productId);
    }

    @Background
    void loadProduct(String productId) {
        try {
            product = productRestClient.findById(productId);
            loadShops(product.getShopIds());
            displayEditProductButtonIfUserOwnsCompany();
            productLoaded();
        } catch (Exception e) {
            FragmentUtil.performBackAction(getActivity());
            e.printStackTrace();
        }
    }

    void displayEditProductButtonIfUserOwnsCompany() {
        try {
            Company company = companyRestClient.findById(product.getCompanyId());
            if (authService.isLoggedIn() && authService.getUserId().equals(company.getUserId())) {
                displayEditProductButton();
            }
        } catch (Exception e) {
            FragmentUtil.performBackAction(getActivity());
            e.printStackTrace();
        }
    }

    @UiThread
    void displayEditProductButton() {
        editProductButton.setVisibility(View.VISIBLE);
    }

    @Click(R.id.editProductButton)
    void editProductClicked() {
        FragmentUtil.setRootFragment(this, ProductManagementFragment_.builder().arg(ProductManagementFragment.PRODUCT, JsonUtils.toJson(product)).build());
    }

    @UiThread
    void initImageSlider(List<String> imageUrls) {
        imageSliderAdapter = new ImageSliderAdapter(getContext(), imageUrls);
        imageSlider.setSliderAdapter(imageSliderAdapter);
        imageSlider.startAutoCycle();
        imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM);
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setScrollTimeInSec(5);
    }

    private void loadShops(List<String> shopIds) {
        try {
            shops = shopRestClient.findByIds(RestParamUtil.toCommaSeparatedStrings(shopIds));
            initShops();
        } catch (Exception e) {
            FragmentUtil.performBackAction(getActivity());
            e.printStackTrace();
        }
    }

    @UiThread
    void initShops() {
        shopListAdapter = new ShopListAdapter(getContext(), imageService);
        shopListAdapter.setItems(shops);
        shopList.setAdapter(shopListAdapter);
    }

    @ItemClick(R.id.shopList)
    void shopClicked(Shop shop) {
        FragmentUtil.setRootFragment(this, ShopDetailFragment_.builder()
                .arg(ShopDetailFragment.SHOP_ID, shop.getId()).build());
    }

    @UiThread
    void productLoaded() {
        getActivity().setTitle(product.getName());
        initProductBasicInfo();
        loadProductImages(product.getId());
    }

    @Background
    @SuppressWarnings("NewApi")
    void loadProductImages(Long productId) {
        try {
            ImageListResponse imageUrls = imageService.getProductImages(productId);
            initImageSlider(imageUrls.stream().map(response -> RestApiUrl.root + RestApiUrl.image + response.getId())
                    .collect(Collectors.toList()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initProductBasicInfo() {
        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(ProductUtil.getPriceString(getContext(), product));
    }
}
