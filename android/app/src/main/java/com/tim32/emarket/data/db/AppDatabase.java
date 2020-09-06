package com.tim32.emarket.data.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import com.tim32.emarket.data.Converters;
import com.tim32.emarket.data.dao.FavoriteDao;
import com.tim32.emarket.data.dao.SettingDao;
import com.tim32.emarket.data.dao.SyncDao;
import com.tim32.emarket.data.entity.Favorite;
import com.tim32.emarket.data.entity.Setting;
import com.tim32.emarket.data.entity.SyncTime;

@Database(entities = {Setting.class, Favorite.class, SyncTime.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract SettingDao settingDao();

    public abstract SyncDao syncDao();

    public abstract FavoriteDao favoriteDao();
}
