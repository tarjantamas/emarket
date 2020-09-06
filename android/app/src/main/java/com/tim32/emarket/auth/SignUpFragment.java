package com.tim32.emarket.auth;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.tim32.emarket.R;
import com.tim32.emarket.product.ProductBrowserFragment_;
import com.tim32.emarket.service.AuthService;
import com.tim32.emarket.shop.ShopsBrowserFragment_;
import com.tim32.emarket.util.FragmentUtil;
import com.tim32.emarket.util.KeyboardUtil;
import org.androidannotations.annotations.*;

@EFragment(R.layout.fragment_sign_up)
public class SignUpFragment extends Fragment {

    private static final String TAG = SignUpFragment.class.getName();

    @ViewById(R.id.emailField)
    EditText emailField;

    @ViewById(R.id.passwordField)
    EditText passwordField;

    @ViewById(R.id.firstNameField)
    EditText firstNameField;

    @ViewById(R.id.lastNameField)
    EditText lastNameField;

    @ViewById(R.id.signUpButton)
    Button signUpButton;

    @ViewById(R.id.skipRegistrationButton)
    Button skipRegistrationButton;

    @Bean
    AuthService authService;

    @Click(R.id.signUpButton)
    void signUpClicked() {
        register(emailField.getText().toString(), passwordField.getText().toString(), firstNameField.getText().toString(), lastNameField.getText().toString());
        KeyboardUtil.hideKeyboard(getActivity());
    }

    @Click(R.id.skipRegistrationButton)
    void skipRegistrationClicked() {
        Log.i(TAG, "Skip registration button clicked");
        FragmentUtil.setRootFragment(this, ProductBrowserFragment_.builder().build());
    }

    @Background
    void register(String email, String password, String firstName, String lastName) {
        try {
            authService.register(email, password, firstName, lastName);
            registrationSuccessful();
            try {
                authService.login(email, password);
                loginSuccess();

            } catch (Exception e) {
                automaticLoginFailed();
            }
        } catch (Exception e) {
            registrationFailed();
        }
    }

    @UiThread
    void automaticLoginFailed() {
        Toast.makeText(getContext(), "Unable to automatically log you in.", Toast.LENGTH_SHORT).show();
        FragmentUtil.setRootFragment(this, SignInFragment_.builder().build());
    }

    @UiThread
    void registrationSuccessful() {
        Toast.makeText(getContext(), "Registration successful.", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void loginSuccess() {
        FragmentUtil.setRootFragment(this, ShopsBrowserFragment_.builder().build());
    }

    @UiThread
    void registrationFailed() {
        Toast.makeText(getContext(), "Unable to register, try again.", Toast.LENGTH_SHORT).show();
    }
}
