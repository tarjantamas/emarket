package com.tim32.emarket.apiclients.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    private Long id;

    private Date updatedAt;

    private boolean deleted;

    private Long shopId;

    private Long userId;
}
