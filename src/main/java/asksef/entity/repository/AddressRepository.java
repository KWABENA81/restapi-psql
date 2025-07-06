package asksef.entity.repository;


import asksef.entity.core.Address;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Transactional
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a from Address a WHERE a.gpsCode=(:code)")
    Collection<Address> findByCode(@Param("code") String code);

    @Query("SELECT a from Address a WHERE a.phone=(:phone)")
    Optional<Address> findByPhone(@Param("phone") String phone);

//    @Query(
//            nativeQuery = true,
//            value = "SELECT ct.city_id, ct.city, ct.country_id, ct.last_update " +
//                    "FROM rest_app.Address adr " +
//                    "INNER JOIN rest_app.City ct ON ct.city_id = adr.city_id WHERE ct.address_id=:addressId")
//    Optional<City> findCityOfAddress(@Param("addressId") Long addressId);

    @Query(
            nativeQuery = true,
            value = "SELECT ct.city_id, ct.city, ct.country_id, ct.last_update, adr.address_id, adr.city_id " +
                    "FROM rest_app.City ct " +
                    "INNER JOIN rest_app.Address adr " +
                    "ON ct.city_id = adr.city_id " +
                    "WHERE adr.address_id=:addressId")
    Optional<Address> findCityOfAddress(@Param("addressId") Long addressId);
}

//@Query(
//        nativeQuery = true,
//        value = "SELECT cty.country_id, cti.city_id, cti.city, cti.last_update FROM rest_app.City cti " +
//                "INNER JOIN rest_app.Country cty ON cti.country_id = cty.country_id WHERE cti.city_id=:id")
//Optional<City> findCityCountry(@Param("id") Long id);