package com.tim32.emarket.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.tim32.emarket.data.entity.Favorite;

import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert
    void insert(Favorite favorite);

    @Update
    void update(Favorite favorite);

    @Query("SELECT * FROM Favorite WHERE userId = :userId AND shopId = :shopId")
    Favorite findByUserIdAndShopId(Long userId, Long shopId);

    @Query("SELECT * FROM Favorite WHERE userId = :userId")
    List<Favorite> findByUserId(Long userId);

    @Query("SELECT * FROM Favorite WHERE userId = :userId AND updatedAt > :syncTime")
    List<Favorite> findByUserIdUpdatedAfterSyncTime(Long userId, long syncTime);
}
