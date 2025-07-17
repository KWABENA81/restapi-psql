package com.asksef.entity.repository;


import com.asksef.entity.core.Invoice;
import com.asksef.entity.core.Item;
import com.asksef.entity.core.Order;
import com.asksef.entity.core.Staff;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface OrderRepository extends JpaRepository<Order, Long>, PagingAndSortingRepository<Order, Long> {

    @Query("SELECT s from Order s WHERE s.orderNr=(:nr)")
    Optional<Order> findByOrderNumber(@Param("nr") String nr);

    @Query(
            nativeQuery = true,
            value = "SELECT v.invoice_id, v.invoice_nr, v.customer_id, v.last_update, s.sale_id " +
                    "FROM rest_app.Invoice v " +
                    "INNER JOIN rest_app.Sale s " +
                    "ON v.invoice_id = s.invoice_id " +
                    "WHERE s.sale_id=:saleId")
    Optional<Invoice> findInvoiceOfOrder(@Param("saleId") Long saleId);

    @Query(
            nativeQuery = true,
            value = "SELECT sf.staff_id, sf.first_name, sf.last_name, sf.username, sf.address_id, sf.last_update, sl.sale_id  " +
                    "FROM rest_app.Staff sf " +
                    "INNER JOIN rest_app.Sale sl " +
                    "ON sf.staff_id = sl.staff_id " +
                    "WHERE sl.sale_id=:saleId")
    Optional<Staff> findStaffOfOrder(@Param("saleId") Long saleId);


    @Query(
            nativeQuery = true,
            value = "SELECT itm.item_id, itm.item_name, itm.item_code, itm.item_desc, itm.item_cost, itm.sale_, sl.sale_id  " +
                    "FROM rest_app.Item itm " +
                    "INNER JOIN rest_app.Sale sl " +
                    "ON sf.staff_id = sl.staff_id " +
                    "WHERE sl.sale_id=:saleId")
    Optional<Item> findItemInOrder(@Param("saleId") Long saleId);
}


