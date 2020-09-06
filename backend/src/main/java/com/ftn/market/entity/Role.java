package com.ftn.market.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.ftn.market.constants.db.DbTableConstants;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = DbTableConstants.ROLES)
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;
}
