package com.ftn.market.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ftn.market.entity.Company;
import com.ftn.market.entity.Product;
import com.ftn.market.entity.Shop;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

  List<Product> findByCompany(Company company);

  Page<Product> findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContaining(String nameTerm, String descriptionTerm,
      Pageable pageable);

  Page<Product> findByNameIgnoreCaseContainingOrDescriptionIgnoreCaseContainingAndIdIn(String nameTerm,
      String descriptionTerm, Set<Long> productIds, Pageable pageable);

  @Query(
      value = "SELECT p FROM Product p WHERE :shop MEMBER p.shops AND ( UPPER (p.name) LIKE CONCAT ('%', UPPER(:term),'%') OR UPPER (p.description) LIKE CONCAT ('%', UPPER(:term),'%') )")
  Page<Product> findByShopAndSearchTerm(@Param("shop") Shop shop, @Param("term") String term, Pageable pageable);
}
