package com.tim32.emarket.auth;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tim32.emarket.R;
import com.tim32.emarket.product.ProductBrowserFragment_;
import com.tim32.emarket.service.AuthService;
import com.tim32.emarket.shop.ShopsBrowserFragment_;
import com.tim32.emarket.util.FragmentUtil;
import com.tim32.emarket.util.KeyboardUtil;
import org.androidannotations.annotations.*;
import timber.log.Timber;

@EFragment(R.layout.fragment_sign_in)
public class SignInFragment extends Fragment {

    @ViewById(R.id.emailField)
    EditText emailField;

    @ViewById(R.id.passwordField)
    EditText passwordField;

    @ViewById(R.id.signInButton)
    Button signInButton;

    @ViewById(R.id.skipSignInButton)
    Button skipSignInButton;

    @ViewById(R.id.signUpHereButton)
    Button signUpHereButton;

    @Bean
    AuthService authService;

    @Click(R.id.signInButton)
    void signInClicked() {
        login(emailField.getText().toString(), passwordField.getText().toString()); //TODO uncomment
//        login("user1@market.com", "password"); //TODO remove
        KeyboardUtil.hideKeyboard(getActivity());
    }

    @Click(R.id.skipSignInButton)
    void skipSignInClicked() {
        FragmentUtil.setRootFragment(this, ProductBrowserFragment_.builder().build());
    }

    @Click(R.id.signUpHereButton)
    void signUpHereClicked() {
        Timber.i("Sign up here clicked");
        FragmentUtil.setRootFragment(this, SignUpFragment_.builder().build());
    }

    @AfterViews
    public void afterViews() {
        if (authService.isLoggedIn()) {
            FragmentUtil.setRootFragment(this, ShopsBrowserFragment_.builder().build());
        }
    }

    @Background
    void login(String email, String password) {
        try {
            authService.login(email, password);
            loginSuccess();
        } catch (Exception e) {
            loginFailure();
        }
    }

    @UiThread
    void loginSuccess() {
        Toast.makeText(getContext(), "Login succeeded", Toast.LENGTH_SHORT).show();
        //this.getActivity().getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE); //todo proveriti da li moze ovako nekako
        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.setVisibility(View.VISIBLE);
        FragmentUtil.setRootFragment(this, ShopsBrowserFragment_.builder().build());
    }

    @UiThread
    void loginFailure() {
        Toast.makeText(getContext(), "Login failed", Toast.LENGTH_SHORT).show();
    }
}
