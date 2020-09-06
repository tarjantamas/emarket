package com.ftn.market.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ftn.market.constants.db.DbColumnConstants;
import com.ftn.market.constants.db.DbTableConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@Table(name = DbTableConstants.PRODUCTS)
@ToString(exclude = { "company", "shops" })
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @Column
  private String description;

  @Column
  private Double price;

  @Column
  private String unit;

  @ManyToOne
  @JoinColumn(name = DbColumnConstants.FK_COMPANY_ID, nullable = false)
  private Company company;

  @ManyToMany(mappedBy = "products", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  private List<Shop> shops = new ArrayList<>();

  public List<Long> getShopIds() {
    return shops.stream().map(Shop::getId).collect(Collectors.toList());
  }
}
