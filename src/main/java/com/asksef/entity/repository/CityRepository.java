package com.asksef.entity.repository;


import com.asksef.entity.core.Address;
import com.asksef.entity.core.City;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("SELECT c from City c WHERE c.city=(:city)")
    City findCityByName(@Param("city") String city);

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
//            value = "SELECT addr FROM rest_app.address addr WHERE addr.city_id=:id")
            value = "SELECT addr.address_id, addr.gps_code, addr.phone, addr.city_id, addr.last_update\n" +
                    "FROM rest_app.city cit\n" +
                    "INNER JOIN rest_app.address addr\n" +
                    "ON cit.city_id = addr.city_id\n" +
                    "WHERE cit.city_id=7")
  //  @Query("SELECT ad from Address ad WHERE ad.city=(:city)")
    List<Address> findAddressesOfCity(@Param("id") Long id);
}
/*
SELECT addr.address_id, addr.gps_code, addr.phone, addr.city_id, addr.last_update
FROM rest_app.city cit
INNER JOIN rest_app.address addr
ON cit.city_id = addr.city_id
WHERE cit.city_id=7;

 value = "SELECT addr.address_id, addr.gps_code, addr.phone, addr.city_id, addr.last_update " +
                    " FROM rest_app.city cit INNER JOIN rest_app.address addr " +
                    " ON cit.city_id = addr.city_id WHERE cit.city_id=:id")
*/
