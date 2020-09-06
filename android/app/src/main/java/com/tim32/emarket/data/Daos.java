package com.tim32.emarket.data;

import android.content.Context;
import com.tim32.EMarketApp;
import com.tim32.emarket.data.dao.FavoriteDao;
import com.tim32.emarket.data.dao.SettingDao;
import com.tim32.emarket.data.dao.SyncDao;
import com.tim32.emarket.data.db.AppDatabase;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class Daos {

    @RootContext
    Context rootContext;

    public AppDatabase getDatabaseInstance() {
        return EMarketApp.appDatabase;
    }

    public SettingDao getSettingDao() {
        return getDatabaseInstance().settingDao();
    }

    public SyncDao getSyncDao() {
        return getDatabaseInstance().syncDao();
    }

    public FavoriteDao getFavoriteDao() {
        return getDatabaseInstance().favoriteDao();
    }
}
