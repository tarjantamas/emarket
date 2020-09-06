package com.ftn.market.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.Date;
import java.util.List;

import com.ftn.market.dto.favorite.FavoriteDto;
import com.ftn.market.entity.Favorite;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ftn.market.dto.favorite.FavoriteResponse;
import com.ftn.market.mapper.FavoriteMapper;
import com.ftn.market.service.FavoriteService;
import com.ftn.market.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorites")
public class FavoritesController {

  private final FavoriteMapper favoriteMapper;
  private final FavoriteService favoriteService;
  private final UserService userService;

  @PutMapping(path = "/shop/{shopId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> addShopToFavorites(@PathVariable final long shopId) {
    favoriteService.addShopToFavorites(userService.getActiveUserOrFail(), shopId);

    return ResponseEntity.noContent().build();
  }

  @GetMapping(value = "/shops", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<FavoriteDto>> findFavoriteShops(@RequestParam(required = false) Long updatedAtFromMS) {
    return ResponseEntity
      .ok(favoriteMapper.mapToFullListResponse(favoriteService.findFavoriteShops(userService.getActiveUserOrFail(), updatedAtFromMS)));
  }
}
