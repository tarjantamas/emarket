package com.tim32.emarket.data.dao;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.tim32.emarket.data.entity.EntityType;
import com.tim32.emarket.data.entity.SyncTime;

@Dao
public interface SyncDao {

    @Insert
    void insert(SyncTime syncTime);

    @Update
    void update(SyncTime syncTime);

    @Query("SELECT * FROM SyncTime WHERE userId = :userId AND entityType = :entityType")
    SyncTime findByUserIdAndEntityType(Long userId, EntityType entityType);
}
