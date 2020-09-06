package com.tim32.emarket.company;

import android.graphics.Color;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.clients.CompanySecureRestClient;
import com.tim32.emarket.apiclients.dto.RegisterCompany;
import com.tim32.emarket.service.AuthService;
import com.tim32.emarket.util.KeyboardUtil;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;


/**
 * A simple {@link Fragment} subclass.
 */
@EFragment(R.layout.fragment_company_registration)
public class CompanyRegistrationFragment extends Fragment {

    @ViewById(R.id.companyNameField)
    EditText companyNameField;

    @ViewById(R.id.companyDescriptionField)
    EditText companyDescriptionField;

    @ViewById(R.id.companyPib)
    EditText companyPib;

    @ViewById(R.id.companyAddress)
    EditText companyAddress;

    @ViewById(R.id.companyCity)
    EditText companyCity;

    @ViewById(R.id.companyCountry)
    EditText companyCountry;

    @ViewById(R.id.companyId)
    EditText companyId;

    @ViewById(R.id.registerCompanyButton)
    Button registerCompanyButton;

    @Bean
    AuthService authService;

    @RestService
    CompanySecureRestClient companySecureRestClient;

    @AfterViews
    void disableButton() {
        getActivity().setTitle(R.string.company_regisration_title);
        registerCompanyButton.setBackgroundColor(Color.GRAY);
    }

    @TextChange(R.id.companyNameField)
    void textChanged() {
        if (companyNameField.length() != 0) {
            registerCompanyButton.setBackgroundColor(Color.parseColor("#03AD53"));
        }
    }

    @Click(R.id.registerCompanyButton)
    void registerCompanyClicked() {
        registerCompany(companyNameField.getText().toString(), companyDescriptionField.getText().toString(),
                companyPib.getText().toString(), companyAddress.getText().toString(), companyCity.getText().toString(), companyCountry.getText().toString(), companyId.getText().toString());
        KeyboardUtil.hideKeyboard(getActivity());
    }

    @Background
    void registerCompany(String companyName, String companyDescription, String companyPib, String companyAddress, String companyCity, String companyCountry, String companyId) {
        try {
            companySecureRestClient.create(RegisterCompany.builder()
                    .name(companyName)
                    .description(companyDescription)
                    .city(companyCity)
                    .vat(Integer.parseInt(companyPib))
                    .rid(Integer.parseInt(companyId))
                    .address(companyAddress)
                    .country(companyCountry)
                    .build());
            registrationSuccessful();
        } catch (Exception e) {
            registrationFailed();
        }
    }

    @UiThread
    void registrationFailed() {
        Toast.makeText(getContext(), "Unable to register, try again.", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void registrationSuccessful() {
        Toast.makeText(getContext(), "Registration successful", Toast.LENGTH_SHORT).show();
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
