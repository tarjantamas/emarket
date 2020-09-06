package com.ftn.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.market.entity.Company;
import com.ftn.market.entity.Image;
import com.ftn.market.entity.Product;
import com.ftn.market.entity.Shop;
import com.ftn.market.entity.User;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

  List<Image> findByCompany(Company company);

  List<Image> findByShop(Shop shop);

  List<Image> findByProduct(Product product);

  List<Image> findByUser(User user);
}
