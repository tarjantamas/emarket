package com.tim32.emarket;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.tim32.emarket.apiclients.config.GlideApp;
import com.tim32.emarket.apiclients.config.RestApiUrl;
import com.tim32.emarket.auth.SignInFragment;
import com.tim32.emarket.auth.SignInFragment_;
import com.tim32.emarket.auth.SignUpFragment;
import com.tim32.emarket.auth.SignUpFragment_;
import com.tim32.emarket.company.CompaniesManagementFragment_;
import com.tim32.emarket.company.CompanyListActivity;
import com.tim32.emarket.company.CompanyListActivity_;
import com.tim32.emarket.customcomponents.gallery.ImageManagementFragment_;
import com.tim32.emarket.product.ProductBrowserFragment_;
import com.tim32.emarket.service.AuthService;
import com.tim32.emarket.service.LocationService;
import com.tim32.emarket.settings.SettingsFragment_;
import com.tim32.emarket.shop.ShopsBrowserFragment;
import com.tim32.emarket.shop.ShopsBrowserFragment_;
import com.tim32.emarket.shop.SubscriptionsAndFavoritesFragment;
import com.tim32.emarket.shop.SubscriptionsAndFavoritesFragment_;
import com.tim32.emarket.user.UserProfileManagementFragment_;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = MainActivity.class.getName();

    @ViewById(R.id.navigationDrawer)
    DrawerLayout navigationDrawer;

    @ViewById(R.id.navigation)
    NavigationView navigationView;

    @ViewById(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @ViewById(R.id.my_toolbar)
    Toolbar toolbar;

    TextView combinedName;

    TextView email;

    ImageView profileImage;

    @Bean
    AuthService authService;

    @Bean
    LocationService locationService;

    private Fragment currentRootFragment;

    @AfterViews
    void afterViews() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        this.combinedName = navigationView.getHeaderView(0).findViewById(R.id.combinedName);
        this.email = navigationView.getHeaderView(0).findViewById(R.id.email);
        this.profileImage = navigationView.getHeaderView(0).findViewById(R.id.profileImage);
        this.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        setOnProfileImageClickedListener();

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(getOnNavigationItemSelectedListener());
        setCurrentRootFragment(SignInFragment_.builder().build());
        backStackChangedListener();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!authService.isLoggedIn()) {
            bottomNavigationView.setVisibility(View.INVISIBLE);
        }
        locationService.listenToPhoneLocationChangesIfAllowed(this);
    }

    private void setOnProfileImageClickedListener() {
        this.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!authService.isLoggedIn()) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("profile_image", "Upload");
                setCurrentRootFragment(ImageManagementFragment_.builder().arg(bundle).build());
                navigationDrawer.close();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getSupportFragmentManager().popBackStackImmediate();
        }
        return true;
    }

    private void backStackChangedListener() {
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            try {
                List<Fragment> fragmentList = getSupportFragmentManager().getFragments();

                if (fragmentList.get(fragmentList.size() - 1) instanceof ShopsBrowserFragment || fragmentList.get(fragmentList.size() - 1) instanceof SubscriptionsAndFavoritesFragment) {
                    bottomNavigationView.setVisibility(View.VISIBLE);
                    if (fragmentList.get(fragmentList.size() - 1) instanceof SubscriptionsAndFavoritesFragment) {
                        bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    } else {
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
                    }
                } else {
                    bottomNavigationView.setVisibility(View.INVISIBLE);
                }
            } catch (Exception ignored) {
            }
        });
    }

    private void logoutClicked() {
        authService.logout();
        updateSideNav();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        setCurrentRootFragment(SignInFragment_.builder().build());
        navigationDrawer.close();
    }

    private void updateSideNav() {
        updateMenuItemsVisibility();
        updateSideNavHeader();
    }

    private void updateSideNavHeader() {
        if (authService.isLoggedIn()) {
            combinedName.setText(authService.getFirstName() + " " + authService.getLastName());
            email.setText(authService.getEmail());
            email.setVisibility(View.VISIBLE);
            loadProfilePicture();
        } else {
            combinedName.setText("Guest");
            email.setText("");
            email.setVisibility(View.INVISIBLE);
            profileImage.setImageResource(R.drawable.ic_account_circle_white);
        }
    }

    private void loadProfilePicture() {
        GlideApp.with(this)
                .load(authService.getAuthorizedImageUrl(RestApiUrl.profileImage))
                .error(R.drawable.ic_account_circle_white)
                .into(profileImage);
    }

    private void updateMenuItemsVisibility() {
        final Menu menu = navigationView.getMenu();
        final boolean isLoggedIn = authService.isLoggedIn();

        for (int i = 0; i < menu.size(); i++) {
            updateMenuItemVisibility(isLoggedIn, menu.getItem(i));
        }
    }

    private void updateMenuItemVisibility(boolean isLoggedIn, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
            case R.id.nav_my_companies:
            case R.id.nav_logout:
            case R.id.nav_subs_and_favs:
            case R.id.nav_settings:
                menuItem.setVisible(isLoggedIn);
                break;
            case R.id.nav_login:
            case R.id.nav_register:
                menuItem.setVisible(!isLoggedIn);
                break;
            default:
                menuItem.setVisible(true);
        }
    }

    public void setCurrentRootFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (currentRootFragment == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        } else {
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_frame, fragment);
            if (!(currentRootFragment instanceof SignInFragment) && !(currentRootFragment instanceof SignUpFragment)) {
                fragmentTransaction
                        .addToBackStack(currentRootFragment.getClass().getName());

            }
            fragmentTransaction.commit();
        }
        currentRootFragment = fragment;

        updateSideNav();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_profile:
                setCurrentRootFragment(UserProfileManagementFragment_.builder().build());
                break;
            case R.id.nav_login:
                setCurrentRootFragment(SignInFragment_.builder().build());
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                break;
            case R.id.nav_my_companies:
                setCurrentRootFragment(CompaniesManagementFragment_.builder().build());
                break;
            case R.id.nav_subs_and_favs:
                setCurrentRootFragment(SubscriptionsAndFavoritesFragment_.builder().build());
                break;
            case R.id.nav_browse_products:
                setCurrentRootFragment(ProductBrowserFragment_.builder().build());
                break;
            case R.id.nav_settings:
                setCurrentRootFragment(SettingsFragment_.builder().build());
                break;
            case R.id.nav_register:
                setCurrentRootFragment(SignUpFragment_.builder().build());
                break;
            case R.id.nav_logout:
                logoutClicked();
                break;
            case R.id.nav_company_details:
                currentRootFragment.startActivity(new Intent(this, CompanyListActivity_.class));
                break;
        }
        navigationDrawer.closeDrawer(GravityCompat.START);
        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener getOnNavigationItemSelectedListener() {
        return item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    setCurrentRootFragment(ShopsBrowserFragment_.builder().build());
                    break;
                case R.id.nav_favorites:
                    setCurrentRootFragment(SubscriptionsAndFavoritesFragment_.builder().build());
                    break;
                case R.id.nav_search:
                    setCurrentRootFragment(ProductBrowserFragment_.builder().build());
                    break;
            }
            return true;
        };
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void updateProfileImage() {
        GlideApp.with(profileImage).load(authService.getAuthorizedImageUrl(RestApiUrl.profileImage)).into(profileImage);
    }
}
