package com.tim32.emarket.service;

import androidx.room.Transaction;
import com.tim32.emarket.apiclients.clients.FavoritesSecureRestClient;
import com.tim32.emarket.data.Daos;
import com.tim32.emarket.data.entity.EntityType;
import com.tim32.emarket.data.entity.Favorite;
import com.tim32.emarket.data.entity.SyncTime;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@EBean(scope = EBean.Scope.Singleton)
public class FavoritesService {

    @Bean
    Daos daos;

    @Bean
    AuthService authService;

    @Bean
    SettingService settingService;

    @RestService
    FavoritesSecureRestClient favoritesSecureRestClient;

    ConcurrentMap<SyncObserver, Boolean> observers = new ConcurrentHashMap<>();

    @AfterInject
    public void afterInject() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!settingService.getSetting().getSyncEnabled()) {
                    return;
                }
                syncFavorites();
                for (SyncObserver observer : observers.keySet()) {
                    observer.onSync();
                }
            }
        }, 0, 5000);
    }

    public boolean isFavorite(long shopId) {
        Favorite favorite = daos.getFavoriteDao().findByUserIdAndShopId(authService.getUserId(), shopId);

        return favorite != null && !favorite.isDeleted();
    }

    @Transaction
    public void add(long shopId) {
        Favorite favorite = daos.getFavoriteDao().findByUserIdAndShopId(authService.getUserId(), shopId);
        if (favorite != null) {
            favorite.setDeleted(false);
            favorite.setUpdatedAt(new Date());
            daos.getFavoriteDao().update(favorite);
        } else {
            favorite = new Favorite(shopId, authService.getUserId(), new Date(), false);
            daos.getFavoriteDao().insert(favorite);
        }
    }

    @Transaction
    public void remove(long shopId) {
        Favorite favorite = daos.getFavoriteDao().findByUserIdAndShopId(authService.getUserId(), shopId);
        if (favorite == null) {
            return;
        }
        favorite.setDeleted(true);
        favorite.setUpdatedAt(new Date());
        daos.getFavoriteDao().update(favorite);
    }


    public List<Favorite> findAll() {
        return daos.getFavoriteDao().findByUserId(authService.getUserId());
    }

    @Transaction
    private void syncFavorites() {
        if (!settingService.getSetting().getSyncEnabled()) {
            return;
        }
        SyncTime syncTime = daos.getSyncDao().findByUserIdAndEntityType(authService.getUserId(), EntityType.FAVORITE);
        List<Favorite> serverSideFavorites = getServerSideFavorites(syncTime);
        List<Favorite> clientSideFavorites = getClientSideFavorites(syncTime);

        calculateAndPerformUpdates(serverSideFavorites, clientSideFavorites);

        updateSyncTime(syncTime);
    }

    private void updateSyncTime(SyncTime syncTime) {
        if (syncTime == null) {
            syncTime = new SyncTime(authService.getUserId(), EntityType.FAVORITE, new Date());
            daos.getSyncDao().insert(syncTime);
            return;
        }
        syncTime.setLastSyncedAt(new Date());
        daos.getSyncDao().update(syncTime);
    }

    private void calculateAndPerformUpdates(List<Favorite> serverSideFavorites, List<Favorite> clientSideFavorites) {
        Map<Long, Favorite> shopIdToClientSideFavorite = groupByShopId(clientSideFavorites);

        for (Favorite serverSideFavorite : serverSideFavorites) {
            Favorite clientSideFavorite = shopIdToClientSideFavorite.get(serverSideFavorite.getShopId());
            if (clientSideFavorite == null) {
                if (daos.getFavoriteDao().findByUserIdAndShopId(authService.getUserId(), serverSideFavorite.getShopId()) == null) {
                    daos.getFavoriteDao().insert(serverSideFavorite);
                } else {
                    daos.getFavoriteDao().update(serverSideFavorite);
                }
            } else if (clientSideFavorite.getUpdatedAt().before(serverSideFavorite.getUpdatedAt())) {
                daos.getFavoriteDao().update(serverSideFavorite);
                shopIdToClientSideFavorite.remove(serverSideFavorite.getShopId());
            } else if (clientSideFavorite.getUpdatedAt().after(serverSideFavorite.getUpdatedAt())) {
                updateServerSideFavorite(clientSideFavorite);
                shopIdToClientSideFavorite.remove(serverSideFavorite.getShopId());
            }
        }

        for (Long shopId : shopIdToClientSideFavorite.keySet()) {
            Favorite clientSideFavorite = shopIdToClientSideFavorite.get(shopId);
            updateServerSideFavorite(clientSideFavorite);
        }
    }

    private void updateServerSideFavorite(Favorite clientSideFavorite) {
        try {
            if (clientSideFavorite.isDeleted()) {
                favoritesSecureRestClient.remove(clientSideFavorite.getShopId());
            } else {
                favoritesSecureRestClient.add(clientSideFavorite.getShopId());
            }
        } catch (Exception ignored) {
        }
    }

    private Map<Long, Favorite> groupByShopId(List<Favorite> favorites) {
        Map<Long, Favorite> map = new HashMap<>();
        for (Favorite favorite : favorites) {
            map.put(favorite.getShopId(), favorite);
        }
        return map;
    }

    private List<Favorite> getClientSideFavorites(SyncTime syncTime) {
        if (syncTime == null) {
            return daos.getFavoriteDao().findByUserId(authService.getUserId());
        } else {
            return daos.getFavoriteDao().findByUserIdUpdatedAfterSyncTime(authService.getUserId(),
                    syncTime.getLastSyncedAt().getTime());
        }
    }

    private List<Favorite> getServerSideFavorites(SyncTime syncTime) {
        try {
            if (syncTime == null) {
                return mapToClientSideFavorites(favoritesSecureRestClient.findAll());
            }
            return mapToClientSideFavorites(favoritesSecureRestClient.findAll(syncTime.getLastSyncedAt().getTime()));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private List<Favorite> mapToClientSideFavorites(List<com.tim32.emarket.apiclients.dto.Favorite> serverSideFavorites) {
        List<Favorite> favorites = new ArrayList<>();

        for (com.tim32.emarket.apiclients.dto.Favorite serverSideFavorite : serverSideFavorites) {
            favorites.add(new Favorite(serverSideFavorite.getShopId(), serverSideFavorite.getUserId(),
                    serverSideFavorite.getUpdatedAt(), serverSideFavorite.isDeleted()));
        }

        return favorites;
    }

    public void unsubscribe(SyncObserver syncObserver) {
        observers.remove(syncObserver);
    }

    public void subscribe(SyncObserver syncObserver) {
        observers.put(syncObserver, true);
    }
}
