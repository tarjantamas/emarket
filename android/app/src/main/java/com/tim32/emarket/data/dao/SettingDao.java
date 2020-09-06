package com.tim32.emarket.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.tim32.emarket.data.entity.Setting;

@Dao
public interface SettingDao {

    @Update
    void update(Setting setting);

    @Query("SELECT * FROM setting WHERE id = :id")
    Setting findById(Long id);

    @Insert
    void insert(Setting setting);
}
