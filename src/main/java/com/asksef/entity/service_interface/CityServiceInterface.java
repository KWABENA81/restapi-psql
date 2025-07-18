package com.asksef.entity.service_interface;


import com.asksef.entity.core.City;

import java.util.Collection;

public interface CityServiceInterface {
    Collection<City> findAll();

    Collection<City> findAll(int pageNumber, int pageSize);

    City findById(Long id);

    City save(City city);

    City update(City city);

    void delete(City city);

    void delete(Long id);

    Long count();

   // CityDTO addCity(CityDTO cityDTO);
}
