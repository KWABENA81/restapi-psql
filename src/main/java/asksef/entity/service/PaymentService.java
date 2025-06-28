package asksef.entity.service;

import asksef.entity.Payment;
import asksef.entity.repository.PaymentRepository;
import asksef.errors.CustomResourceExistsException;
import asksef.errors.CustomResourceNotFoundException;
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

    @Override
    public Payment save(Payment payment) {
        Optional<Payment> optional = this.paymentRepository.findById(payment.getPaymentId());

        if (optional.isPresent()) {
            throw new CustomResourceExistsException("Payment", "id", null, payment.getPaymentId());
        }
        return this.paymentRepository.save(payment);
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
}
