package asksef.entity.repository;


import asksef.entity.Address;
import asksef.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {

//    @Query("SELECT c from Customer c WHERE c.lastName LIKE %:names% OR c.firstName LIKE %:names")
//    Collection<Customer> findLikeNames(@Param("names") String names);

    @Query(
            nativeQuery = true,
            value = "SELECT adr.address_id, adr.gps_code, adr.phone, adr.city_id, adr.last_update, cust.customer_id " +
                    "FROM rest_app.Address adr " +
                    "INNER JOIN rest_app.Customer cust " +
                    "ON adr.address_id = cust.address_id " +
                    "WHERE cust.customer_id=:customerId")
    Optional<Address> findAddressOfCustomer(@Param("customerId") Long customerId);
}


