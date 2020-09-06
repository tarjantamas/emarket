package com.tim32.emarket.company;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.google.gson.Gson;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.CompanySecureRestClient;
import com.tim32.emarket.apiclients.clients.ShopSecureRestClient;
import com.tim32.emarket.apiclients.dto.Company;
import com.tim32.emarket.apiclients.dto.ImageResponse;
import com.tim32.emarket.apiclients.dto.Shop;
import com.tim32.emarket.customcomponents.gallery.ImageManagementFragment_;
import com.tim32.emarket.customcomponents.shop.ShopListItem;
import com.tim32.emarket.service.ImageService;
import com.tim32.emarket.shop.EditShopFragment;
import com.tim32.emarket.shop.EditShopFragment_;
import com.tim32.emarket.shop.ShopRegistrationFragment_;
import com.tim32.emarket.util.FragmentUtil;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@EFragment(R.layout.fragment_company_management)
public class CompanyManagementFragment extends Fragment {

    public static final String COMPANY_ID = "company_id";

    private static final String SHOP_ID = "shop_id";

    @ViewById(R.id.shopsListView)
    ListView shopsListView;

    @RestService
    CompanySecureRestClient companySecureRestClient;

    @RestService
    ShopSecureRestClient shopSecureRestClient;

    private ArrayAdapter<ShopListItem> listAdapter;

    @ViewById(R.id.companyNameField)
    TextView companyNameField;

    @ViewById(R.id.companyAddressField)
    TextView companyAddressField;

    ImageService imageService;

    private Company company;

    @AfterViews
    void afterViews() {
        getActivity().setTitle(R.string.company_management_title);
        getCompanyDetails();
        Context context = getContext();
        if (context == null) {
            return;
        }
        initShopListAdapter(context);
        loadShops();
    }

    private void initShopListAdapter(Context context) {
        listAdapter = new ArrayAdapter<>(context, R.layout.text_view_list_item, new ArrayList<>());
        shopsListView.setAdapter(listAdapter);
    }

    @Click(R.id.registerShopButton)
    void registerShopButtonClicked() {
        if (company != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(COMPANY_ID, this.getArguments().getLong(COMPANY_ID));
            FragmentUtil.setRootFragment(this, ShopRegistrationFragment_.builder().build(), bundle);
        }
    }

    @ItemClick(R.id.shopsListView)
    void shopClicked(ShopListItem shopListItem) {
        Shop shop = shopListItem.getShop();
        FragmentUtil.setRootFragment(this, EditShopFragment_.builder().arg(EditShopFragment.SHOP, new Gson().toJson(shop)).build());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Click(R.id.imageManagementButton)
    void imageManagementButtonClicked() {
        FragmentUtil.setRootFragment(this, ImageManagementFragment_.builder().arg(COMPANY_ID, getArguments().getLong(COMPANY_ID)).build());

    }

    @Background
    void loadShops() {
        List<Shop> shops = shopSecureRestClient.findByCompanyId(this.getArguments().getLong(COMPANY_ID));
        setShops(shops);
    }

    @SuppressWarnings("NewApi")
    @UiThread
    void setShops(List<Shop> shops) {
        listAdapter.clear();
        listAdapter.addAll(shops.stream().map(ShopListItem::new).collect(Collectors.toList()));
    }

    @Background
    void getCompanyDetails() {
        try {
            Bundle bundle = this.getArguments();
            long companyId = bundle.getLong(COMPANY_ID);
            company = companySecureRestClient.findById(companyId);
            displayDetails(company);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Unable to load company details", Toast.LENGTH_SHORT).show();
            FragmentUtil.performBackAction(getActivity());
        }
    }

    @UiThread
    void displayDetails(Company company) {
        if (company == null) {
            return;
        }
        companyNameField.setText(company.getName());
        companyAddressField.setText(company.getAddress());
    }

}
