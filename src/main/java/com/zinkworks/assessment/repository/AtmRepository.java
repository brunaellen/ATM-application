package com.zinkworks.assessment.repository;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.zinkworks.assessment.model.Atm;

@Repository
public interface AtmRepository extends JpaRepository<Atm, Long> {
  Atm findAllById(Long id);

  @Modifying(clearAutomatically = true)
  @Query("update Atm a set a.balance = ?1 where a.id = ?2")
  void updateAtmBalance(BigDecimal updatedBalance, Long id);
}
