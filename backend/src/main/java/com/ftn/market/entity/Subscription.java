package com.ftn.market.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ftn.market.constants.db.DbColumnConstants;
import com.ftn.market.constants.db.DbTableConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = DbTableConstants.SUBSCRIPTIONS)
public class Subscription {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = DbColumnConstants.FK_COMPANY_ID, nullable = false)
  private Company company;

  @ManyToOne
  @JoinColumn(name = DbColumnConstants.FK_USER_ID, nullable = false)
  private User user;
}
