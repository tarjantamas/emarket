package com.tim32.emarket.user;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.dto.User;
import com.tim32.emarket.apiclients.dto.UserUpdateRequest;
import com.tim32.emarket.auth.SignInFragment_;
import com.tim32.emarket.service.AuthService;
import com.tim32.emarket.shop.ShopsBrowserFragment_;
import com.tim32.emarket.util.FragmentUtil;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.time.Duration;

@EFragment(R.layout.fragment_user_profile_management)
public class UserProfileManagementFragment extends Fragment {

    @Bean
    AuthService authService;

    @ViewById(R.id.firstNameField)
    TextView firstName;

    @ViewById(R.id.displayEmail)
    TextView email;

    @ViewById(R.id.curentPasswordField)
    TextView currentPassword;

    @ViewById(R.id.newPasswordField)
    TextView newPassword;

    @ViewById(R.id.lastNameField)
    TextView lastName;

    @AfterViews
    void afterViews() {
        populateFields();
    }

    private void populateFields() {
        if (authService.isLoggedIn()) {
            getActivity().setTitle(getString(R.string.profile_management_title));
            firstName.setText(authService.getFirstName());
            email.setText(authService.getEmail());
            lastName.setText(authService.getLastName());
        } else {
            firstName.setText("Error");
        }
    }

    @Click(R.id.saveChangesButton)
    void saveChangesClicked(){
        updateUser();
    }

    @Background
    void updateUser() {
        try {
            if(authService.checkPassword(authService.getEmail(), currentPassword.getText().toString())){
                successToast();
                UserUpdateRequest userUpdateRequest = new UserUpdateRequest();
                userUpdateRequest.firstName = firstName.getText().toString();
                userUpdateRequest.lastName = lastName.getText().toString();
                userUpdateRequest.password = newPassword.getText().toString();
                try{
                    authService.updateUser(userUpdateRequest);
                }
                catch (Exception e){
                    exceptionToast();
                }

            }
        } catch (Exception e) {
            failToast();
        }
    }

    @UiThread
    void successToast() {
        Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void failToast() {
        Toast.makeText(getContext(), "Current password WRONG", Toast.LENGTH_SHORT).show();
    }

    @UiThread
    void exceptionToast() {
        Toast.makeText(getContext(), "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
    }


}
