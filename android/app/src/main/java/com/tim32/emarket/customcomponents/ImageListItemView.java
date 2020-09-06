package com.tim32.emarket.customcomponents;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tim32.emarket.R;
import com.tim32.emarket.apiclients.config.GlideApp;
import com.tim32.emarket.service.AuthService;
import com.tim32.emarket.service.FavoritesService;
import com.tim32.emarket.service.SyncObserver;
import org.androidannotations.annotations.*;

@EViewGroup(R.layout.image_list_item_view)
public class ImageListItemView extends ConstraintLayout implements SyncObserver {

    @ViewById(R.id.titleTextView)
    TextView titleTextView;

    @ViewById(R.id.headerImage)
    ImageView itemImageView;

    @ViewById(R.id.favoriteButton)
    FloatingActionButton favoriteButton;

    @Bean
    AuthService authService;

    @Bean
    FavoritesService favoritesService;

    private long shopId;

    public ImageListItemView(Context context) {
        super(context);
    }

    @AfterViews
    void afterViews() {
        if (!authService.isLoggedIn()) {
            favoriteButton.hide();
        }
        observeSyncEvents();
    }

    @Background
    void observeSyncEvents() {
        favoritesService.subscribe(this);
    }

    public void bind(String title, String imageUrl, long shopId) {
        this.shopId = shopId;
        titleTextView.setText(title);
        GlideApp.with(this).load(imageUrl).error(R.drawable.placeholder).into(itemImageView);
        if (authService.isLoggedIn()) {
            checkIfShopIsFavorite(shopId);
        }
    }

    @Click(R.id.favoriteButton)
    void favoriteButtonClicked() {
        toggleFavoriteForShop(shopId);
    }

    @Background
    void toggleFavoriteForShop(long shopId) {
        try {
            if (favoritesService.isFavorite(shopId)) {
                favoritesService.remove(shopId);
            } else {
                favoritesService.add(shopId);
            }
            checkIfShopIsFavorite(shopId);
        } catch (Exception e) {
            toggleFavoriteForShopFailed();
        }
    }

    @UiThread
    void toggleFavoriteForShopFailed() {
        Toast.makeText(getContext(), "Unable to communicate with server", Toast.LENGTH_SHORT).show();
    }

    @Background
    void checkIfShopIsFavorite(long shopId) {
        setFavoriteIcon(favoritesService.isFavorite(shopId));
    }

    @UiThread
    void setFavoriteIcon(boolean isFavorite) {
        if (favoriteButton == null) {
            return;
        }
        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.favorite);
        } else {
            favoriteButton.setImageResource(R.drawable.favorite_grey);
        }
    }

    @Override
    public void onSync() {
        checkIfShopIsFavorite(shopId);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    void onDestroy() {
        favoritesService.unsubscribe(this);
    }
}
