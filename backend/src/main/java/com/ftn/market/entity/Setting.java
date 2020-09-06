package com.ftn.market.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.ftn.market.constants.db.DbColumnConstants;
import com.ftn.market.constants.db.DbTableConstants;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@Entity
@ToString(exclude = { "user" })
@Table(name = DbTableConstants.SETTINGS)
public class Setting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private Boolean syncEnabled = false;

  @Column
  private Boolean locationTrackingAllowed = false;

  @Column
  private Double searchRadius = 1.0;

  @Column
  private Boolean emailsEnabled = false;

  @Column
  private Date updatedAt;

  @OneToOne
  @JoinColumn(name = DbColumnConstants.FK_USER_ID)
  private User user;
}
