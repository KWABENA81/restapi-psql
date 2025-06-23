package asksef.entity.repository;


import asksef.entity.Invoice;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
//    Invoice updateInvoice(Invoice invoice);
}
