package com.tim32.emarket.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Setting {

    @PrimaryKey
    private Long id;

    private Boolean syncEnabled;

    private Boolean locationTrackingAllowed;

    private Double searchRadius;

    private Boolean emailsEnabled;

    private Date updatedAt;
}
