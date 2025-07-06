package asksef.entity.repository;


import asksef.entity.core.Invoice;
import asksef.entity.core.Payment;
import asksef.entity.core.Staff;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p from Payment p WHERE p.paymentNr=(:pn)")
    Payment findByPaymentNr(@Param("pn") String nr);

    @Query(
            nativeQuery = true,
            value = "SELECT s.staff_id, s.first_name, s.last_name, s.address_id, s.username, s.last_update, p.staff_id " +
                    "FROM rest_app.Staff s  " +
                    "INNER JOIN rest_app.Payment p " +
                    "ON s.staff_id = p.staff_id " +
                    "WHERE p.payment_id=:id")
    Optional<Staff> findStaffOnPayment(@Param("id") Long id);

    @Query(
            nativeQuery = true,
            value = "SELECT i.invoice_id, i.invoice_nr, i.customer_id, i.last_update, p.invoice_id " +
                    "FROM rest_app.Invoice i  " +
                    "INNER JOIN rest_app.Payment p " +
                    "ON i.invoice_id = p.invoice_id " +
                    "WHERE p.payment_id=:id")
    Optional<Invoice> findInvoiceOnPayment(@Param("id") Long id);
}
