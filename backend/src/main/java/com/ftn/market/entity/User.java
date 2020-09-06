package com.ftn.market.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = DbTableConstants.USERS)
@ToString(exclude = { "companies", "roles", "password" })
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column
  private String firstName;

  @Column
  private String lastName;

  @OneToMany(mappedBy = "user")
  private List<Company> companies = new ArrayList<>();

  public List<Long> getCompanyIds() {
    return companies.stream().map(Company::getId).collect(Collectors.toList());
  }

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = DbTableConstants.USERS_ROLES,
      joinColumns = @JoinColumn(name = DbColumnConstants.FK_USER_ID),
      inverseJoinColumns = @JoinColumn(name = DbColumnConstants.FK_ROLE_ID))
  private Set<Role> roles;
}
