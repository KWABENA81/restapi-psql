package asksef.entity.service_interface;


import asksef.entity.core.Country;

import java.util.Collection;

public interface CountryServiceInterface {
    Collection<Country> findAll();

    Collection<Country> findAll(int pageNumber, int pageSize);

    Country findById(Long id);

    Country save(Country country);

    Country update(Country country);

    void delete(Country country);

    Long count();

    //  Country update(Long id, Country newCountry);
}
