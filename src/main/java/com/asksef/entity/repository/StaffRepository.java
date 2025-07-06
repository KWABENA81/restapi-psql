package com.asksef.entity.repository;


import com.asksef.entity.core.Staff;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Transactional
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("SELECT s from Staff s WHERE s.firstName=(:names) OR s.lastName=(:names)")
    Collection<Staff> findByNames(@Param("names") String names);

    @Query("SELECT s from Staff s WHERE s.username=(:username)")
    Optional<Staff> findByUsername(@Param("username") String username);

//    @Query(
//            nativeQuery = true,
//            value = "SELECT adr.address_id, adr.gps_code, adr.phone, adr.city_id, adr.last_update, st.staff_id " +
//                    "FROM rest_app.Address adr " +
//                    "INNER JOIN rest_app.Staff st " +
//                    "ON adr.address_id = st.address_id " +
//                    "WHERE st.staff_id=:staffId")
//    Optional<Address> findAddressOfCustomer(@Param("staffId") Long staffId);
}
