package com.ftn.market.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ftn.market.constants.db.DbColumnConstants;
import com.ftn.market.constants.db.DbTableConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString(exclude = { "shops", "user" })
@Table(name = DbTableConstants.COMPANIES)
public class Company {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @Column
  private String description;

  @Column
  private Integer vat;

  @Column
  private Integer rid;

  @Column
  private String country;

  @Column
  private String city;

  @Column
  private String address;

  @OneToMany(mappedBy = "company")
  private List<Shop> shops = new ArrayList<>();

  public List<Long> getShopIds() {
    return shops.stream().map(Shop::getId).collect(Collectors.toList());
  }

  @ManyToOne
  @JoinColumn(name = DbColumnConstants.FK_USER_ID, nullable = false)
  private User user;
}
