package com.tim32.emarket.company;

import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.dto.Company;
import com.tim32.emarket.service.CompaniesService;
import com.tim32.emarket.util.BundleUtil;
import org.androidannotations.annotations.*;

@EFragment(R.layout.fragment_company_detail)
public class CompanyDetailFragment extends Fragment {
    public static final String ARG_ITEM_ID = "item_id";

    @Bean
    CompaniesService companiesService;

    @ViewById(R.id.name)
    TextView name;

    @ViewById(R.id.description)
    TextView description;

    @ViewById(R.id.address)
    TextView address;

    @AfterViews
    void afterViews() {
        Long companyId = BundleUtil.getLongOrNull(getArguments(), ARG_ITEM_ID);
        if (companyId != null) {
            loadCompany(companyId);
        }
    }

    @Background
    void loadCompany(long companyId) {
        Company company = companiesService.findById(companyId);
        if (company != null) {
            updateView(company);
        }
    }

    @UiThread
    void updateView(Company company) {
        name.setText(company.getName());
        description.setText(company.getDescription());
        address.setText(company.getAddress());
    }
}
