package com.asksef.entity.service_interface;


import com.asksef.entity.core.Order;

import java.util.Collection;

public interface SaleServiceInterface {
    Collection<Order> findAll();

    Collection<Order> findAll(int pageNumber, int pageSize);

    Order findById(Long id);

    Order save(Order order);

    Order update(Order order);

    void delete(Order order);

    Long count();
}
