package com.zinkworks.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zinkworks.assessment.model.ATMOperation;

@Repository
public interface ATMOperationRepository extends JpaRepository<ATMOperation, Long>  {
}
