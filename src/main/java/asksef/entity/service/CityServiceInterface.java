package asksef.entity.service;


import asksef.entity.City;

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
