package com.asksef.entity.repository;


import com.asksef.entity.core.Customer;
import com.asksef.entity.core.Invoice;
import com.asksef.entity.core.Order;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface InvoiceRepository extends JpaRepository<Invoice, Long>, PagingAndSortingRepository<Invoice, Long> {
    @Query(
            nativeQuery = true,
            value = "SELECT c.customer_id, c.first_name, c.last_name, c.create_date, c.address_id, c.last_update, v.invoice_id " +
                    "FROM rest_app.Customer c  " +
                    "INNER JOIN rest_app.Invoice v " +
                    "ON c.custmer_id = v.customer_id " +
                    "WHERE v.invoice_id=:id")
    Optional<Customer> findCustomerOnInvoice(@Param("id") Long id);

    @Query(
            nativeQuery = true,
            value = "SELECT o.order_id, o.order_date, o.item_id, o.order_nr, o.staff_id, o.last_update, v.invoice_id " +
                    "FROM rest_app.Order o  " +
                    "INNER JOIN rest_app.Invoice v " +
                    "ON o.order_id = v.order_id " +
                    "WHERE v.invoice_id=:id")
    Optional<Order> findOrderInvoice(@Param("id") Long id);
}

