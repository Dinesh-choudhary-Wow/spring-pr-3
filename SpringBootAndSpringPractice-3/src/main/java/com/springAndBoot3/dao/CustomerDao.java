package com.springAndBoot3.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.springAndBoot3.Entity.Customer;

public interface CustomerDao extends JpaRepository<Customer, Long> {
	
}
