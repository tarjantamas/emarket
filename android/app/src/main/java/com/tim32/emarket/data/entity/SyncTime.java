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
@Entity(primaryKeys = {"userId", "entityType"})
public class SyncTime {

    @NonNull
    private Long userId;

    @NonNull
    private EntityType entityType;

    private Date lastSyncedAt;
}
