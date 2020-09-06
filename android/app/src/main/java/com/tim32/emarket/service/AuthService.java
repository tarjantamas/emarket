package com.tim32.emarket.service;

import android.util.Log;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.tim32.emarket.apiclients.clients.AuthRestClient;
import com.tim32.emarket.apiclients.clients.UserSecureRestClient;
import com.tim32.emarket.apiclients.config.AuthStore;
import com.tim32.emarket.apiclients.dto.*;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.HttpClientErrorException;

@EBean
public class AuthService {

    public static final String TAG = AuthService.class.getName();

    @RestService
    AuthRestClient authRestClient;

    @RestService
    UserSecureRestClient userSecureRestClient;

    @Bean
    AuthStore authStore;

    public boolean isLoggedIn() {
        return authStore.getToken() != null;
    }

    public void logout() {
        authStore.removeToken();
    }

    public void login(String email, String password) throws Exception {
        try {
            LoginResponse loginResponse = authRestClient.login(LoginRequest.builder().email(email).password(password).build());
            authStore.setToken(loginResponse.getToken());
        } catch (HttpClientErrorException e) {
            Log.d(TAG, "Unable to login due to: " + e.getResponseBodyAsString());
            throw new Exception("Unable to login", e);
        }
        try {
            User user = userSecureRestClient.getLoggedInUser();
            authStore.setCurrentUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkPassword(String email, String password) throws Exception {
        try {
            LoginResponse loginResponse = authRestClient.login(LoginRequest.builder().email(email).password(password).build());
            authStore.setToken(loginResponse.getToken());
            return true;
        } catch (HttpClientErrorException e) {
            Log.d(TAG, "Wrong password: " + e.getResponseBodyAsString());
            throw new Exception("Wrong password", e);
        }
    }

    public void updateUser(UserUpdateRequest userUpdateRequest) throws Exception {
        try {
            User user = userSecureRestClient.updateUser(userUpdateRequest);
        } catch (HttpClientErrorException e) {
            Log.d(TAG, "Wrong password: " + e.getResponseBodyAsString());
            throw new Exception("Wrong password", e);
        }
        try {
            User user = userSecureRestClient.getLoggedInUser();
            authStore.setCurrentUser(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void register(String email, String password, String firstName, String lastName) throws Exception {
        try {
            authRestClient.register(RegisterUser.builder()
                    .email(email)
                    .password(password)
                    .firstName(firstName)
                    .lastName(lastName)
                    .build());
        } catch (Exception e) {
            throw new Exception("Unable to register", e);
        }
    }

    public String getFirstName() {
        return authStore.getFirstName();
    }

    public String getLastName() {
        return authStore.getLastName();
    }

    public String getEmail() {
        return authStore.getEmail();
    }

    public Long getUserId() {
        return authStore.getId();
    }

    public String getToken() {
        return authStore.getToken();
    }

    public GlideUrl getAuthorizedImageUrl(String url) {
        return new GlideUrl(url, new LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer " + getToken())
                .build());
    }
}
