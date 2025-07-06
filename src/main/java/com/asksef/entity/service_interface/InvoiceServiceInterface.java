package com.asksef.entity.service_interface;


import com.asksef.entity.core.Invoice;

import java.util.Collection;

public interface InvoiceServiceInterface {
    Collection<Invoice> findAll();

    Collection<Invoice> findAll(int pageNumber, int pageSize);

    Invoice findById(Long id);

    Invoice save(Invoice invoice);

    Invoice update(Invoice invoice);

    void delete(Invoice invoice);

    Long count();
}