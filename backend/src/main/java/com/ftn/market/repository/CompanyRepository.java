package com.ftn.market.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.market.entity.Company;
import com.ftn.market.entity.User;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

  List<Company> findByUser(User user);
}
