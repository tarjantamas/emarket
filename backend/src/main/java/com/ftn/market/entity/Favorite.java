package com.ftn.market.entity;

import javax.persistence.*;

import com.ftn.market.constants.db.DbColumnConstants;
import com.ftn.market.constants.db.DbTableConstants;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = DbTableConstants.FAVORITES)
public class Favorite {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Date updatedAt;

  @Column(nullable = false)
  private boolean deleted;

  @ManyToOne
  @JoinColumn(name = DbColumnConstants.FK_SHOP_ID, nullable = false)
  private Shop shop;

  @ManyToOne
  @JoinColumn(name = DbColumnConstants.FK_USER_ID, nullable = false)
  private User user;
}
