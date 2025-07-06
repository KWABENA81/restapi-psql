package com.asksef.entity.service_interface;


import com.asksef.entity.core.Payment;

import java.util.Collection;

public interface PaymentServiceInterface {
    Collection<Payment> findAll();

    Collection<Payment> findAll(int pageNumber, int pageSize);

    Payment findById(Long id);

    Payment save(Payment payment);

    Payment update(Payment payment);

    void delete(Payment payment);

    Long count();
}
