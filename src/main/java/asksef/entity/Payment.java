package asksef.entity;

import asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Setter
@Entity
@Table(name = "PAYMENT", schema = "rest_app")
public class Payment implements Serializable, Comparable<Payment> {

    private static final Logger log = LoggerFactory.getLogger(Payment.class);

    public Payment() {
        this.paymentNr = String.valueOf(Math.abs(LocalDateTime.now().hashCode()));
    }

    public Payment(Long id) {
        this();
        this.paymentId = id;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_id_seq")
    @SequenceGenerator(name = "payment_id_seq", sequenceName = "payment_payment_id_seq", allocationSize = 1)
    @Column(name = "PAYMENT_ID", nullable = false)
    private Long paymentId;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID")
    @JsonBackReference
    private Staff staff;

    public void setStaff(Staff staff) {
        this.staff = staff;
        staff.addPayment(this);
    }

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_ID", referencedColumnName = "INVOICE_ID")
    @JsonBackReference
    private Invoice invoice;

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
        invoice.addPayment(this);
    }

    @Getter
    @Setter
    @Column(name = "PAYMENT_NR", nullable = false, length = 128)
    private String paymentNr;

    @Getter
    @Setter
    @Column(name = "AMOUNT")
    private Float amount;

    @Getter
    @Setter
    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;

    @Convert(converter = DateConverter.class)
    private LocalDateTime lastUpdate;

    @Basic(optional = false)
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(invoice, payment.invoice) && Objects.equals(amount, payment.amount)
                && Objects.equals(paymentNr, payment.paymentNr) && Objects.equals(paymentDate, payment.paymentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoice, amount, paymentNr, paymentDate);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId=" + paymentId +
                ", staff=" + staff +
                ", invoice=" + invoice +
                ", amount=" + amount +
                ", confirmationNr='" + paymentNr + '\'' +
                ", paymentDate=" + paymentDate +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    public int compareTo(Payment payment) {
        return this.paymentNr.compareTo(payment.getPaymentNr());
    }
}