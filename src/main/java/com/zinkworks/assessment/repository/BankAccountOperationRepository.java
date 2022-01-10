package com.zinkworks.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zinkworks.assessment.model.BankAccountOperation;

@Repository
public interface BankAccountOperationRepository extends JpaRepository<BankAccountOperation, Long>  {
}
