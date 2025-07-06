package com.asksef.entity.service_interface;


import com.asksef.entity.core.Customer;

import java.util.Collection;

public interface CustomerServiceInterface {
    Collection<Customer> findAll();

    Collection<Customer> findAll(int pageNumber, int pageSize);

    Customer findById(Long id);

    Customer save(Customer customer);

    Customer update(Customer customer);

    void delete(Customer customer);

    Long count();
}
