package com.springAndBoot3.service;

import java.util.List;

import com.springAndBoot3.Entity.Customer;

public interface CustomerService {
	List<Customer> getAllCustomers();
    Customer getCustomerById(Long id);
    Customer createCustomer(Customer customer);
    Customer updateCustomer(Long id, Customer customer);
}
