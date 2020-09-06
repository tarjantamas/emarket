package com.ftn.market.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@ToString(exclude = { "company", "products" })
@Table(name = DbTableConstants.SHOPS)
public class Shop {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @Column
  private String description;

  @Column
  private Double latitude;

  @Column
  private Double longitude;

  @ManyToOne
  @JoinColumn(name = DbColumnConstants.FK_COMPANY_ID, nullable = false)
  private Company company;

  @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
  @JoinTable(
      name = DbTableConstants.PRODUCTS_SHOPS,
      joinColumns = @JoinColumn(name = DbColumnConstants.FK_SHOP_ID),
      inverseJoinColumns = @JoinColumn(name = DbColumnConstants.FK_PRODUCT_ID))
  private List<Product> products = new ArrayList<>();
}
