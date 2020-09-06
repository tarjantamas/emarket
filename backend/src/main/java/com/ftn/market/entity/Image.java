package com.ftn.market.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.util.StringUtils;

import com.ftn.market.constants.db.DbColumnConstants;
import com.ftn.market.constants.db.DbTableConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@Table(name = DbTableConstants.IMAGES)
public class Image {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String uuid;

  @Column
  private String extension;

  @ManyToOne
  @JoinColumn(name = DbColumnConstants.FK_COMPANY_ID)
  private Company company;

  @ManyToOne
  @JoinColumn(name = DbColumnConstants.FK_SHOP_ID)
  private Shop shop;

  @ManyToOne
  @JoinColumn(name = DbColumnConstants.FK_PRODUCT_ID)
  private Product product;

  @ManyToOne
  @JoinColumn(name = DbColumnConstants.FK_USER_ID)
  private User user;

  public String getFileNameWithExtension() {
    if (StringUtils.isEmpty(extension)) {
      return uuid;
    }

    return String.format("%s.%s", uuid, extension);
  }

  public boolean isCompanyImage() {
    return company != null;
  }

  public boolean isShopImage() {
    return shop != null;
  }

  public boolean isProductImage() {
    return product != null;
  }
}
