package com.ftn.market.dto.favorite;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDto {

    private Long id;

    private Date updatedAt;

    private boolean deleted;

    private Long shopId;

    private Long userId;
}
