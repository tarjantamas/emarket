package com.ftn.market.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.market.entity.Favorite;
import com.ftn.market.entity.Shop;
import com.ftn.market.entity.User;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

  Optional<Favorite> findByUserAndShop(User user, Shop shop);

  List<Favorite> findByUserAndUpdatedAtIsAfter(User user, Date updatedAt);

  List<Favorite> findByUser(User user);
}
