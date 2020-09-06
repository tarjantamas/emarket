package com.ftn.market.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.market.entity.Company;
import com.ftn.market.entity.Subscription;
import com.ftn.market.entity.User;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  Optional<Subscription> findByUserAndCompany(User user, Company company);

  List<Subscription> findByUserAndCompanyIsNotNull(User user);

  List<Subscription> findByCompanyAndUserIsNotNull(Company company);
}
