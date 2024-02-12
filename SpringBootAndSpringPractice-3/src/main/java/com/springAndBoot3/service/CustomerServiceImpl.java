package com.springAndBoot3.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springAndBoot3.Entity.Customer;
import com.springAndBoot3.dao.CustomerDao;

@Service
public class CustomerServiceImpl implements CustomerService{
	@Autowired
    private CustomerDao customerDao;

    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.findAll();
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerDao.findById(id).orElse(null);
    }

    @Override
    public Customer createCustomer(Customer customer) {
        return customerDao.save(customer);
    }

    @Override
    public Customer updateCustomer(Long id, Customer customer) {
        if (!customerDao.existsById(id)) {
            return null;
        }
        customer.setCustomerId(id);
        return customerDao.save(customer);
    }
}
