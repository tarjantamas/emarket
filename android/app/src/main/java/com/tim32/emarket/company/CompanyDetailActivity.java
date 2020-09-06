package com.tim32.emarket.company;

import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.dto.Company;
import com.tim32.emarket.service.CompaniesService;
import com.tim32.emarket.util.BundleUtil;
import org.androidannotations.annotations.*;

@EActivity(R.layout.activity_company_detail)
public class CompanyDetailActivity extends AppCompatActivity {

    public static final String ARG_ITEM_ID = "item_id";

    @Bean
    CompaniesService companiesService;

    @ViewById(R.id.name)
    TextView name;

    @ViewById(R.id.description)
    TextView description;

    @ViewById(R.id.address)
    TextView address;

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @AfterViews
    void afterViews() {
        setSupportActionBar(toolbar);
        Long companyId = BundleUtil.getLongOrNull(getIntent().getExtras(), ARG_ITEM_ID);
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
        toolbar.setTitle(company.getName());
    }
}
