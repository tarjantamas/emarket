package com.tim32.emarket.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.ProductRestClient;
import com.tim32.emarket.apiclients.clients.ShopSecureRestClient;
import com.tim32.emarket.apiclients.dto.Shop;
import com.tim32.emarket.customcomponents.gallery.ImageManagementFragment_;
import com.tim32.emarket.customcomponents.map.LocationPickerActivity;
import com.tim32.emarket.customcomponents.map.LocationPickerActivity_;
import com.tim32.emarket.product.ManageShopProductsActivity;
import com.tim32.emarket.product.ManageShopProductsActivity_;
import com.tim32.emarket.service.ImageProvider;
import com.tim32.emarket.util.BundleUtil;
import com.tim32.emarket.util.DataValidatingToaster;
import com.tim32.emarket.util.FragmentUtil;
import com.tim32.emarket.util.JsonUtils;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.List;

@EFragment(R.layout.fragment_edit_shop)
public class EditShopFragment extends Fragment {

    public static final String SHOP = "shop";

    public static final int LOCATION_PICKER_ACTIVITY_REQUEST_CODE = 1;

    public static final int MANAGE_PRODUCTS_REQUEST_CODE = 2;

    @ViewById(R.id.name)
    EditText name;

    @ViewById(R.id.description)
    EditText description;

    @ViewById(R.id.longitude)
    EditText longitude;

    @ViewById(R.id.manageImagesButton)
    Button manageImagesButton;

    @ViewById(R.id.latitude)
    EditText latitude;

    @RestService
    ShopSecureRestClient shopSecureRestClient;

    @RestService
    ProductRestClient productRestClient;

    @Bean
    ImageProvider imageProvider;

    private Shop shop;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.edit_shop);
    }

    @SuppressLint("SetTextI18n")
    @AfterViews
    void afterViews() {
        shop = BundleUtil.fromJsonExtraOrNull(getArguments(), SHOP, Shop.class);
        if (shop == null) {
            return;
        }

        name.setText(shop.getName());
        description.setText(shop.getDescription());
        longitude.setText(shop.getLongitude().toString());
        latitude.setText(shop.getLatitude().toString());
    }

    @Click(R.id.chooseLocationButton)
    void selectLocationClicked() {
        LocationPickerActivity_.intent(this)
                .extra(LocationPickerActivity.LAT, shop.getLatitude())
                .extra(LocationPickerActivity.LNG, shop.getLongitude())
                .startForResult(LOCATION_PICKER_ACTIVITY_REQUEST_CODE);
    }

    @Click(R.id.manageProductsButton)
    void addProductClicked() {
        ManageShopProductsActivity_.intent(this).extra(ManageShopProductsActivity.SHOP, JsonUtils.toJson(shop))
                .startForResult(MANAGE_PRODUCTS_REQUEST_CODE);
    }

    @Click(R.id.manageImagesButton)
    void manageImagesButtonClicked() {
        Long l = shop.getId();
        FragmentUtil.setRootFragment(this, ImageManagementFragment_.builder().arg("shop_id", shop.getId()).build());
    }

    @OnActivityResult(MANAGE_PRODUCTS_REQUEST_CODE)
    void onProductsUpdated(int resultCode, Intent data) {
        if (resultCode != ManageShopProductsActivity.RESULT_CODE_SELECTED) {
            return;
        }
        List<Long> productIds = JsonUtils.fromJson(data.getStringExtra(ManageShopProductsActivity.SELECTED_PRODUCT_IDS), JsonUtils.LONG_LIST_TYPE);
        shop.setProductIds(productIds);
        updateShop();
    }

    @SuppressLint("SetTextI18n")
    @OnActivityResult(LOCATION_PICKER_ACTIVITY_REQUEST_CODE)
    void onLocationPickerResult(Intent data) {
        if (data == null) {
            return;
        }
        double lng = data.getDoubleExtra("lng", 0);
        double lat = data.getDoubleExtra("lat", 0);
        latitude.setText(Double.toString(lat));
        longitude.setText(Double.toString(lng));
    }

    @Click(R.id.updateShopButton)
    void updateShopClicked() {
        String description = this.description.getText().toString();
        String name = this.name.getText().toString();
        String lng = this.longitude.getText().toString();
        String lat = this.latitude.getText().toString();

        new DataValidatingToaster(getContext())
                .rule(() -> StringUtils.isNotEmpty(name), "Please provide a name")
                .rule(() -> StringUtils.isNotEmpty(description), "Please provide a description")
                .rule(() -> NumberUtils.isCreatable(lng), "Please provide a valid longitude")
                .rule(() -> NumberUtils.isCreatable(lat), "Please provide a valid latitude")
                .validateAllRules();

        shop.setDescription(description);
        shop.setName(name);
        shop.setLatitude(Double.parseDouble(lat));
        shop.setLongitude(Double.parseDouble(lng));

        updateShop();
    }

    @Background
    @SuppressWarnings("NewApi")
    void updateShop() {
        try {
            shopSecureRestClient.update(shop.getId(), shop);
            shopUpdateSuccess();
        } catch (Exception e) {
            e.printStackTrace();
            shopUpdateFailure();
        }
    }

    @UiThread
    void shopUpdateFailure() {
        Toast.makeText(getContext(), "Unable to update shop", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void shopUpdateSuccess() {
        Toast.makeText(getContext(), "Shop updated", Toast.LENGTH_SHORT).show();
    }
}
