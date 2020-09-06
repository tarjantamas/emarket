package com.tim32.emarket.apiclients.config;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.tim32.emarket.apiclients.dto.User;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;
import org.androidannotations.annotations.RootContext;

@EBean(scope = Scope.Singleton)
public class AuthStore {

    private final static String AUTH_STORE = "auth_store";

    private final static String AUTH_STORE_TOKEN = "token";

    private final static String AUTH_CURRENT_USER = "current_user";

    @RootContext
    Context context;

    private String token;

    private User currentUser;

    public String getToken() {
        if (token == null) {
            token = getTokenFromSharedPreferences();
        }
        return token;
    }

    public void setToken(String token) {
        this.token = token;
        saveTokenToSharedPreferences(token);
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            currentUser = getUserFromSharedPreferences();
        }
        return currentUser;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        saveUserToSharedPreferences(user);
    }

    private void saveTokenToSharedPreferences(String token) {
        SharedPreferences sharedPreferences = context
                .getApplicationContext()
                .getSharedPreferences(AUTH_STORE, Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(AUTH_STORE_TOKEN, token)
                .apply();
    }

    private String getTokenFromSharedPreferences() {
        SharedPreferences sharedPreferences = context
                .getApplicationContext()
                .getSharedPreferences(AUTH_STORE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(AUTH_STORE, null);
    }

    private void saveUserToSharedPreferences(User user) {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context
                .getApplicationContext()
                .getSharedPreferences(AUTH_STORE, Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .putString(AUTH_CURRENT_USER, gson.toJson(user))
                .apply();
    }

    private User getUserFromSharedPreferences() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context
                .getApplicationContext()
                .getSharedPreferences(AUTH_STORE, Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString(AUTH_STORE, null);
        if (userJson == null) {
            return null;
        }
        return gson.fromJson(userJson, User.class);
    }

    public void removeToken() {
        token = null;
        currentUser = null;
        SharedPreferences sharedPreferences = context
                .getApplicationContext()
                .getSharedPreferences(AUTH_STORE, Context.MODE_PRIVATE);
        sharedPreferences
                .edit()
                .remove(AUTH_STORE_TOKEN)
                .apply();
    }

    public String getFirstName() {
        return currentUser.getFirstName();
    }

    public String getLastName() {
        return currentUser.getLastName();
    }

    public String getEmail() {
        return currentUser.getEmail();
    }

    public Long getId() {
        return  currentUser.getId();
    }
}
