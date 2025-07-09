package com.asksef.entity.service_impl;


import com.asksef.entity.core.Address;
import com.asksef.entity.core.Customer;
import com.asksef.entity.core.Invoice;
import com.asksef.entity.model.CustomerModel;
import com.asksef.entity.repository.CustomerRepository;
import com.asksef.entity.service_interface.CustomerServiceInterface;
import com.asksef.errors.CustomResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CustomerService implements CustomerServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(CustomerService.class);
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Collection<Customer> findAll() {
        log.info("Find all customers");
        return customerRepository.findAll();
    }

    @Override
    public Collection<Customer> findAll(int pageNumber, int pageSize) {
        log.info("Find all customers from page {} of {}", pageNumber, pageSize);
        return customerRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public Customer findById(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Customer", "id", null, id)
        );
        log.info("Found customer with id {}", id);
        return customer;
    }

    @Transactional
    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer save(@Valid CustomerModel custModel) {
        Customer customer = Customer.builder()
                .creationtime(custModel.getCreationDate())
                .address(custModel.getAddress())
                .firstName(custModel.getFirstName())
                .lastName(custModel.getLastName())
                .lastUpdate(custModel.getLastUpdate())
                .build();
        return save(customer);
    }

    @Override
    public Customer update(Customer customer) {
        Long id = customer.getCustomerId();
        Optional<Customer> optionalCust = this.customerRepository.findById(id);
        if (optionalCust.isEmpty()) {
            throw new CustomResourceNotFoundException("Customer", "id", null, id);
        } else {
            Customer customer1 = optionalCust.get();

            customer1.setFirstName(customer.getFirstName());
            customer1.setLastName(customer.getLastName());
            customer1.setAddress(customer.getAddress());
            customer1.setInvoiceList(customer.getInvoiceList());
            log.info("Updating customer: {}", customer1);
            return customerRepository.save(customer1);
        }
    }

    public Customer update(Long id, Customer newCust) {
        if (Objects.equals(newCust.getCustomerId(), id)) {
            return update(newCust);
        } else {
            throw new CustomResourceNotFoundException("Customer", "id", null, id);
        }
    }

    @Override
    public Long count() {
        return customerRepository.count();
    }

    @Override
    public void delete(Customer customer) {
        delete(customer.getCustomerId());
    }

    public void delete(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Customer", "id", null, id)
        );
        log.info("Deleting customer with id {}", id);
        customerRepository.delete(customer);
    }

    public Address findAddressOfCustomer(Long id) {
        Optional<Address> addressOptional = this.customerRepository.findAddressOfCustomer(id);
        if (addressOptional.isEmpty()) {
            throw new CustomResourceNotFoundException("customer", "id", null, id);
        }
        return addressOptional.get();
    }

    public List<Invoice> findCustomerInvoices(Long id) {
        Customer customer = this.customerRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Country", "id", null, id)
        );
        return customer.getInvoiceList();
    }
}
