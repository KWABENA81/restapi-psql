package com.asksef.entity.service_interface;


import com.asksef.entity.core.Address;

import java.util.Collection;

public interface AddressServiceInterface {
    Collection<Address> findAll();

    Collection<Address> findAll(int pageNumber, int pageSize);

    Address save(Address address);

    Address findById(Long id);

    Address update(Address address);

    void delete(Address address);

    Long count();
}
