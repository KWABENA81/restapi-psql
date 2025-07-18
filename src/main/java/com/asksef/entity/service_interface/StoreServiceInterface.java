package com.asksef.entity.service_interface;


import com.asksef.entity.core.Store;

import java.util.Collection;

public interface StoreServiceInterface {
    Collection<Store> findAll();

    Collection<Store> findAll(int pageNumber, int pageSize);

    Store findById(Long id);

    Store save(Store store);

    Store update(Store store);

    void delete(Store store);

    Long count();
}
