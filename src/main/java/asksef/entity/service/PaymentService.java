package asksef.entity.service;

import asksef.entity.Invoice;
import asksef.entity.Payment;
import asksef.entity.Staff;
import asksef.entity.entity_model.PaymentModel;
import asksef.entity.repository.PaymentRepository;
import asksef.errors.CustomResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;


@Service
public class PaymentService implements PaymentServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Collection<Payment> findAll() {
        log.info("Finding all payments");
        return this.paymentRepository.findAll();
    }

    @Override
    public Collection<Payment> findAll(int pageNumber, int pageSize) {
        return this.paymentRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public Payment findById(Long id) {
        Payment payment = this.paymentRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Payment", "id", null, id)
        );
        log.info("Found payment with id: {}", id);
        return payment;
    }

    @Transactional
    @Override
    public Payment save(Payment payment) {
        return this.paymentRepository.save(payment);
    }

    @Transactional
    public Payment save(@Valid PaymentModel paymentModel) {
        Payment payment = Payment.builder()
                .id(paymentModel.getPaymentId())
                .paymentNr(paymentModel.getPaymentNr())
                .paymentDate(paymentModel.getPaymentDate())
                .amount(paymentModel.getAmount())
                .lastUpdate(paymentModel.getLastUpdate())
                .invoice(paymentModel.getInvoice())
                .staff(paymentModel.getStaff())
                .build();
        return this.save(payment);
    }

    @Override
    public Payment update(Payment payment) {
        Long id = payment.getPaymentId();
        Optional<Payment> optional = this.paymentRepository.findById(id);

        if (optional.isPresent()) {
            Payment updatePayment = optional.get();
            updatePayment.setPaymentNr(payment.getPaymentNr());
            updatePayment.setAmount(payment.getAmount());
            updatePayment.setPaymentDate(payment.getPaymentDate());

            updatePayment.setStaff(payment.getStaff());
            updatePayment.setInvoice(payment.getInvoice());

            log.info("Updating payment with id: {}", payment.getPaymentId());
            return this.paymentRepository.save(updatePayment);
        } else {
            throw new CustomResourceNotFoundException("Payment", "id", null, payment.getPaymentId());
        }
    }

    public Payment update(Long id, Payment newPayment) {
        if (Objects.equals(newPayment.getPaymentId(), id)) {
            return update(newPayment);
        } else
            throw new CustomResourceNotFoundException("Payment", "id", null, id);
    }

    public void delete(Long id) {
        Payment payment = this.paymentRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Payment", "id", null, id)
        );
        this.paymentRepository.delete(payment);
        log.info("Deleted payment with id: {}", id);
    }

    @Override
    public void delete(Payment payment) {
        delete(payment.getPaymentId());
    }

    @Override
    public Long count() {
        return this.paymentRepository.count();
    }

    public Payment findByPayNr(String pn) {
        return this.paymentRepository.findByPaymentNr(pn);
    }

    public Invoice findInvoiceOnPayment(Long id) {
        return this.paymentRepository.findInvoiceOnPayment(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Invoice", "id", null, id)
        );
    }

    public Staff findStaffOnPayment(Long id) {
        return this.paymentRepository.findStaffOnPayment(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Staff", "id", null, id)
        );
    }
}
