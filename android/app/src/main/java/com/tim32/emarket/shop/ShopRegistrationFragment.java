package com.tim32.emarket.shop;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.ShopSecureRestClient;
import com.tim32.emarket.apiclients.dto.Shop;
import com.tim32.emarket.customcomponents.map.LocationPickerActivity_;
import com.tim32.emarket.util.DataValidatingToaster;
import com.tim32.emarket.util.FragmentUtil;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_shop_registration)
public class ShopRegistrationFragment extends Fragment {

    public static final String COMPANY_ID = "company_id";

    public static final int LOCATION_PICKER_ACTIVITY_REQUEST_CODE = 1;

    @ViewById(R.id.shopNameField)
    EditText name;

    @ViewById(R.id.shopDescriptionField)
    EditText description;

    @ViewById(R.id.longitudeField)
    EditText longitude;

    @ViewById(R.id.latitudeField)
    EditText latitude;

    @ViewById(R.id.registerShopButton)
    Button registerShopButton;

    @RestService
    ShopSecureRestClient shopSecureRestClient;

    Long companyId;

    @AfterViews
    void afterViews() {
        getActivity().setTitle(R.string.shop_regisration_title);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            companyId = bundle.getLong(COMPANY_ID);
        }
    }

    @Click(R.id.registerShopButton)
    void registerShopClicked() {
        if (companyId == null) {
            Toast.makeText(getContext(), "Unable to register shop.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean areFieldsValid = new DataValidatingToaster(getContext())
                .rule(() -> StringUtils.isNotEmpty(name.getText().toString()), "Please provide a name")
                .rule(() -> StringUtils.isNotEmpty(description.getText().toString()), "Please provide a description")
                .rule(() -> NumberUtils.isCreatable(longitude.getText().toString()), "Please provide a valid longitude")
                .rule(() -> NumberUtils.isCreatable(latitude.getText().toString()), "Please provide a valid latitude")
                .validateAllRules();

        if (!areFieldsValid) {
            return;
        }

        registerShop(name.getText().toString(), description.getText().toString(), Double.valueOf(longitude.getText().toString()),
                Double.valueOf(latitude.getText().toString()), companyId);
    }

    @Background
    void registerShop(String name, String description, Double longitude, Double latitude, long companyId) {
        try {
            shopSecureRestClient.create(Shop.builder()
                    .name(name)
                    .description(description)
                    .longitude(longitude)
                    .latitude(latitude)
                    .companyId(companyId)
                    .build());
            shopRegistrationSucceeded();

            FragmentUtil.performBackAction(getActivity());
        } catch (Exception e) {
            shopRegistrationFailed();
        }
    }

    @UiThread
    void shopRegistrationSucceeded() {
        Toast.makeText(getContext(), "Shop registered", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void shopRegistrationFailed() {
        Toast.makeText(getContext(), "Unable to register shop", Toast.LENGTH_SHORT).show();
    }

    @Click(R.id.chooseLocationButton)
    void selectLocationClicked() {
        LocationPickerActivity_.intent(this).startForResult(LOCATION_PICKER_ACTIVITY_REQUEST_CODE);
    }

    @SuppressLint("SetTextI18n")
    @OnActivityResult(LOCATION_PICKER_ACTIVITY_REQUEST_CODE)
    void onLocationPickerResult(Intent data) {
        double lng = data.getDoubleExtra("lng", 0);
        double lat = data.getDoubleExtra("lat", 0);
        latitude.setText(Double.toString(lat));
        longitude.setText(Double.toString(lng));
    }
}
