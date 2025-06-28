package asksef.entity.service;


import asksef.entity.Customer;

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
