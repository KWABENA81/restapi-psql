package asksef.entity.repository;


import asksef.entity.Payment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query("SELECT p from Payment p WHERE p.paymentNr=(:pn)")
    Payment findByPaymentNr(@Param("pn") String nr);

}
