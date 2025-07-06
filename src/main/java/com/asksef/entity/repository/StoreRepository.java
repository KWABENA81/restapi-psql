package com.asksef.entity.repository;


import com.asksef.entity.core.Address;
import com.asksef.entity.core.Staff;
import com.asksef.entity.core.Store;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Transactional
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s from Store s WHERE s.storeName LIKE %:name%")
    Collection<Store> findByStoreName(@Param("name") String name);

    @Query(
            nativeQuery = true,
            value = "SELECT adr.address_id, adr.gps_code, adr.phone, adr.city_id, adr.last_update, st.store_id " +
                    "FROM rest_app.Address adr " +
                    "INNER JOIN rest_app.Store st " +
                    "ON adr.address_id = st.address_id " +
                    "WHERE st.store_id=:storeId")
    Optional<Address> findAddressOfStore(@Param("storeId") Long storeId);

    @Query(
            nativeQuery = true,
            value = "SELECT sf.staff_id, sf.first_name, sf.last_name, sf.address_id, sf.username, sf.last_update, st.store_id " +
                    "FROM rest_app.Staff sf " +
                    "INNER JOIN rest_app.Store st " +
                    "ON sf.staff_id = st.staff_id " +
                    "WHERE st.store_id=:storeId")
    Optional<Staff> findStaffOfStore(@Param("storeId") Long storeId);
}
