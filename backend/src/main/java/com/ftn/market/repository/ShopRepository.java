package com.ftn.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.market.entity.Company;
import com.ftn.market.entity.Shop;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

  List<Shop> findByCompany(Company company);
}
