package com.ftn.market.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ftn.market.constants.ErrorCodes;
import com.ftn.market.entity.Favorite;
import com.ftn.market.entity.Shop;
import com.ftn.market.entity.User;
import com.ftn.market.exception.BadRequestException;
import com.ftn.market.repository.FavoriteRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

  private final ShopService shopService;
  private final FavoriteRepository favoriteRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.SERIALIZABLE)
  public void addShopToFavorites(final User user, final long shopId) {
    final Shop shop = shopService.findById(shopId, ErrorCodes.SHOP_NOT_FOUND_3);

    final Optional<Favorite> oFavorite = favoriteRepository.findByUserAndShop(user, shop);
    if (oFavorite.isPresent()) {
      Favorite oldFavorite = oFavorite.get();
      oldFavorite.setDeleted(false);
      oldFavorite.setUpdatedAt(new Date());
      favoriteRepository.save(oldFavorite);
      log.info("{} successfully added (revived) {} to favorites", user, shop);
      return;
    }

    final Favorite favorite = new Favorite();
    favorite.setUser(user);
    favorite.setShop(shop);
    favorite.setUpdatedAt(new Date());
    favorite.setDeleted(false);

    favoriteRepository.save(favorite);
    log.info("{} successfully added {} to favorites", user, shop);
  }

  @Transactional(readOnly = true, isolation = Isolation.SERIALIZABLE)
  public List<Favorite> findFavoriteShops(final User user, Long updatedAtMS) {
    Date updatedAt = null;
    if (updatedAtMS != null) {
      updatedAt = new Date(updatedAtMS);
    }

    if (updatedAt != null) {
      return favoriteRepository.findByUserAndUpdatedAtIsAfter(user, updatedAt);
    }

    return favoriteRepository.findByUser(user);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public void removeShopFromFavorites(final User user, final long shopId) {
    final Shop shop = shopService.findById(shopId, ErrorCodes.SHOP_NOT_FOUND_4);

    final Optional<Favorite> oFavorite = favoriteRepository.findByUserAndShop(user, shop);
    if (!oFavorite.isPresent()) {
      throw new BadRequestException(ErrorCodes.SHOP_NOT_FAVORITE);
    }

    Favorite oldFavorite = oFavorite.get();
    oldFavorite.setDeleted(true);
    oldFavorite.setUpdatedAt(new Date());
    favoriteRepository.save(oldFavorite);
    log.info("{} successfully removed {} from favorites", user, shop);
  }
}
