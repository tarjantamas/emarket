package com.tim32.emarket.shop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.CompanyRestClient;
import com.tim32.emarket.apiclients.clients.CompanySecureRestClient;
import com.tim32.emarket.apiclients.clients.SubscriptionsSecureRestClient;
import com.tim32.emarket.apiclients.dto.Company;
import com.tim32.emarket.apiclients.dto.Subscription;
import com.tim32.emarket.company.CompanyManagementFragment_;
import com.tim32.emarket.service.AuthService;
import com.tim32.emarket.util.FragmentUtil;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;


@EFragment(R.layout.fragment_shop_detail_company_tab)
public class ShopDetailCompanyTabFragment extends Fragment {

    public static String COMPANY_ID = "company_id";

    @ViewById(R.id.name)
    TextView name;

    @ViewById(R.id.description)
    TextView description;

    @ViewById(R.id.subscribeToCompanyButton)
    Button subscribeToCompanyButton;

    @ViewById(R.id.companyManagementButton)
    Button companyManagementButton;

    @ViewById(R.id.unsubscribeFromCompanyButton)
    Button unsubscribeFromCompanyButton;

    @ViewById(R.id.shopListButton)
    Button shopListButton;

    @RestService
    CompanyRestClient companyRestClient;

    @RestService
    CompanySecureRestClient companySecureRestClient;

    @RestService
    SubscriptionsSecureRestClient subscriptionsSecureRestClient;

    @Bean
    AuthService authService;

    List<Subscription> subscriptions;

    @Click(R.id.companyManagementButton)
    void companyManagementClicked() {
        FragmentUtil.setRootFragment(this, CompanyManagementFragment_.builder().arg(getArguments()).build());
    }

    @Click(R.id.subscribeToCompanyButton)
    void subscribeToCompanyClicked() {
        subscribeToCompany();
    }

    @Click(R.id.shopListButton)
    void shopListButtonClicked() {
        Bundle bundle = new Bundle();
        bundle.putLong(COMPANY_ID, getArguments().getLong(COMPANY_ID));
        FragmentUtil.setRootFragment(this, ShopsBrowserByCompanyFragment_.builder().build(), bundle);
    }

    @Click(R.id.unsubscribeFromCompanyButton)
    void unsubscribeFromCompanyClicked() {
        unsubscribeFromCompany();
    }

    @Background
    void unsubscribeFromCompany() {
        try {
            subscriptionsSecureRestClient.unsubscribe(getArguments().getLong(COMPANY_ID));
            unsubscribeFromCompanySucceeded();
        } catch (Exception e) {
            unsubscribeFromCompanyFailed();
        }
    }

    @UiThread
    void unsubscribeFromCompanyFailed() {
        Toast.makeText(getContext(), "Failed to unsubscribe from company", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void unsubscribeFromCompanySucceeded() {
        Toast.makeText(getContext(), "Unsubscribe succeeded", Toast.LENGTH_SHORT).show();
        unsubscribeFromCompanyButton.setVisibility(View.GONE);
        subscribeToCompanyButton.setVisibility(View.VISIBLE);
    }

    @Background
    void subscribeToCompany() {
        try {
            subscriptionsSecureRestClient.subscribe(getArguments().getLong(COMPANY_ID));
            subscribeToCompanySucceeded();
        } catch (Exception e) {
            subscribeToCompanyFailed();
        }
    }

    @UiThread
    void subscribeToCompanySucceeded() {
        Toast.makeText(getContext(), "Subscribed to company", Toast.LENGTH_SHORT).show();
        subscribeToCompanyButton.setVisibility(View.GONE);
        unsubscribeFromCompanyButton.setVisibility(View.VISIBLE);
    }

    @UiThread
    void subscribeToCompanyFailed() {
        Toast.makeText(getContext(), "Failed to subscribe to company", Toast.LENGTH_SHORT).show();
    }

    @AfterViews
    void afterViews() {
        Bundle bundle = getArguments();
        if (bundle == null) {
            return;
        }

        try {
            loadCompany(bundle.getLong(COMPANY_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }

        getCompaniesSubscriptions();
    }

    @Background
    void getCompaniesSubscriptions() {
        if (!authService.isLoggedIn()) {
            hideSubscriptionRelatedButtons();
            return;
        }
        subscriptions = subscriptionsSecureRestClient.findAllCompaniesSubscriptions();
        for (Subscription subscription : subscriptions) {
            if (subscription.getCompany().getId() == getArguments().getLong(COMPANY_ID)) {
                showUnsubscribe();
            }
        }
    }

    @UiThread
    void hideSubscriptionRelatedButtons() {
        subscribeToCompanyButton.setVisibility(View.GONE);
        unsubscribeFromCompanyButton.setVisibility(View.GONE);
    }

    @UiThread
    void showUnsubscribe() {
        subscribeToCompanyButton.setVisibility(View.GONE);
        unsubscribeFromCompanyButton.setVisibility(View.VISIBLE);
    }

    @Background
    void loadCompany(long companyId) {
        try {
            companyLoaded(companySecureRestClient.findById(companyId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void companyLoaded(Company company) {
        setCompany(company);
    }

    private void setCompany(Company company) {
        name.setText(company.getName());
        description.setText(company.getDescription());
        if (!authService.isLoggedIn() || !company.getUserId().equals(authService.getUserId())) {
            companyManagementButton.setVisibility(View.GONE);
        }
    }
}
