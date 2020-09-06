package com.tim32;

import androidx.room.Room;
import com.tim32.emarket.data.db.AppDatabase;

public class EMarketApp extends android.app.Application {

    public static final String DB_NAME = "mojabaza";

    public static AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        appDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, DB_NAME).build();
    }
}
