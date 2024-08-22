package com.study.contactapi.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.study.contactapi.domain.user.Login;

import jakarta.transaction.Transactional;

public interface LoginRepository extends JpaRepository<Login, String>{
  Optional<Login> findByEmail(String email);
  
  @Transactional  
  @Modifying
  @Query(value = "UPDATE login SET account_activated_at = NOW() WHERE id = ?1", nativeQuery = true)
  void activateUserLoginById(String id);
}
