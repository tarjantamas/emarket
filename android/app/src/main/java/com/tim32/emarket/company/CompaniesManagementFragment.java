package com.tim32.emarket.company;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.CompanySecureRestClient;
import com.tim32.emarket.apiclients.dto.Company;
import com.tim32.emarket.customcomponents.company.CompanyListItem;
import com.tim32.emarket.util.FragmentUtil;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@EFragment(R.layout.fragment_companies_management)
public class CompaniesManagementFragment extends Fragment {

    @ViewById(R.id.companiesListView)
    ListView companiesListView;

    @RestService
    CompanySecureRestClient companySecureRestClient;

    private ArrayAdapter<CompanyListItem> listAdapter;

    @AfterViews
    void afterViews() {
        getActivity().setTitle(R.string.companies_management_fragment_title);
        Context context = getContext();
        if (context == null) {
            return;
        }
        initCompanyListAdapter(context);
        loadCompanies();
    }

    @Click(R.id.addCompanyButton)
    void addCompanyClicked() {
        FragmentUtil.setRootFragment(this, CompanyRegistrationFragment_.builder().build());
    }

    @ItemClick(R.id.companiesListView)
    void companyClicked(CompanyListItem companyListItem) {
        Company company = companyListItem.getCompany();
        Bundle bundle = new Bundle();
        bundle.putLong(CompanyManagementFragment.COMPANY_ID, company.getId());
        FragmentUtil.setRootFragment(this, CompanyManagementFragment_.builder().build(), bundle);
    }

    private void initCompanyListAdapter(Context context) {
        listAdapter = new ArrayAdapter<>(context, R.layout.text_view_list_item, new ArrayList<>());
        companiesListView.setAdapter(listAdapter);
    }

    @Background
    void loadCompanies() {
        try {
            List<Company> companies = companySecureRestClient.findLoggedUsersCompanies();
            setCompanies(companies);
        } catch (Exception e) {
            setCompanies(Collections.emptyList());
        }
    }

    @UiThread
    @SuppressWarnings("NewApi")
    void setCompanies(List<Company> companies) {
        listAdapter.clear();
        listAdapter.addAll(companies.stream().map(CompanyListItem::new).collect(Collectors.toList()));
    }
}
