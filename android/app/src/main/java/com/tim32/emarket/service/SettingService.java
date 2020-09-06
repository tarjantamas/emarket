package com.tim32.emarket.service;

import android.content.Context;
import com.tim32.emarket.apiclients.clients.SettingsSecureRestClient;
import com.tim32.emarket.data.Daos;
import com.tim32.emarket.data.entity.Setting;
import org.androidannotations.annotations.*;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@EBean
public class SettingService {

    @Bean
    Daos daos;

    @Bean
    AuthService authService;

    @RestService
    SettingsSecureRestClient settingsSecureRestClient;

    @RootContext
    Context rootContext;

    ConcurrentMap<SyncObserver, Boolean> observers = new ConcurrentHashMap<>();

    @AfterInject
    void afterInject() {
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!getSetting().getSyncEnabled()) {
                    return;
                }
                syncSetting();
                for (SyncObserver observer : observers.keySet()) {
                    observer.onSync();
                }
            }
        }, 0, 20000);
    }

    public Setting getSetting() {
        Setting currentSetting = daos.getSettingDao().findById(authService.getUserId());
        if (currentSetting == null) {
            currentSetting = createDefaultSetting();
            try {
                daos.getSettingDao().insert(currentSetting);
            } catch (Exception ignored) {
            }
        }
        return currentSetting;
    }

    public void updateSetting(Setting setting) {
        setting.setUpdatedAt(new Date());
        updateLocalSetting(setting);
    }

    private void syncSetting() {
        Setting currentSetting = daos.getSettingDao().findById(authService.getUserId());
        syncSetting(currentSetting);
    }

    @Background
    void syncSetting(Setting localSetting) {
        if (!localSetting.getSyncEnabled()) {
            return;
        }
        try {
            Setting serverSideSetting = settingsSecureRestClient.getUserSettings();
            if (serverSideSetting.getUpdatedAt() != null &&
                    serverSideSetting.getUpdatedAt().after(localSetting.getUpdatedAt())) {
                updateLocalSetting(serverSideSetting);
            } else if (serverSideSetting.getUpdatedAt() == null ||
                    serverSideSetting.getUpdatedAt().before(localSetting.getUpdatedAt())) {
                updateServerSideSetting(localSetting);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateServerSideSetting(Setting setting) {
        try {
            settingsSecureRestClient.updateUserSettings(setting);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Setting createDefaultSetting() {
        return new Setting(authService.getUserId(), true, true, 15D, true, new Date());
    }

    private void updateLocalSetting(Setting setting) {
        setting.setId(authService.getUserId());
        daos.getSettingDao().update(setting);
    }

    public void unsubscribe(SyncObserver syncObserver) {
        observers.remove(syncObserver);
    }

    public void subscribe(SyncObserver syncObserver) {
        observers.put(syncObserver, true);
    }
}
