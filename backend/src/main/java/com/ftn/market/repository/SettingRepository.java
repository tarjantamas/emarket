package com.ftn.market.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ftn.market.entity.Setting;
import com.ftn.market.entity.User;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {

  Optional<Setting> findByUser(User user);
}
