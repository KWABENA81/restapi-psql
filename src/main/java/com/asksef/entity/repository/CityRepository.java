package com.asksef.entity.repository;


import com.asksef.entity.core.Address;
import com.asksef.entity.core.City;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CityRepository extends JpaRepository<City, Long>, PagingAndSortingRepository<City, Long> {

    @Query("SELECT c from City c WHERE c.city=(:city)")
    Optional<City> findCityByName(@Param("city") String city);

    @Query(
            nativeQuery = true,
            value = "SELECT cty.country_id, cty.country, cty.last_update, cti.city_id, cti.city " +
                    "FROM rest_app.Country cty  " +
                    "INNER JOIN rest_app.City cti " +
                    "ON cti.country_id = cty.country_id " +
                    "WHERE cti.city_id=:id")
    Optional<City> findCountryOfCity(@Param("id") Long id);


    @Query(
            nativeQuery = true,
            value = "SELECT addr.address_id, addr.gps_code, addr.phone, addr.city_id, addr.last_update\n" +
                    "FROM rest_app.city cit\n" +
                    "INNER JOIN rest_app.address addr\n" +
                    "ON cit.city_id = addr.city_id\n" +
                    "WHERE cit.city_id=7")
    List<Address> findAddressesOfCity(@Param("id") Long id);
}