package com.promineo.pet_store.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.promineo.pet_store.entity.Customer;


public interface CustomerDao extends JpaRepository<Customer, Long> {

}