package com.tim32.emarket.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(primaryKeys = {"shopId", "userId"})
public class Favorite {

    @NonNull
    private Long shopId;

    @NonNull
    private Long userId;

    private Date updatedAt;

    private boolean deleted;
}
