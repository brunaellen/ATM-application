package com.zinkworks.assessment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zinkworks.assessment.model.Atm;

public interface AtmRepository extends JpaRepository<Atm, Long> {
}
