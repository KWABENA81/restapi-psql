package asksef.entity;

import asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Setter
@Entity
@Table(name = "SALE", schema = "rest_app")
public class Sale implements Serializable, Comparable<Sale> {
    private static final Logger log = LoggerFactory.getLogger(Sale.class);

    public Sale() {
        this.lastUpdate = LocalDateTime.now();
        this.saleNr = String.valueOf(Math.abs(LocalDateTime.now().hashCode()));
        this.saleDate = new Timestamp(System.currentTimeMillis());
    }

    public Sale(String saleNr) {
        this.saleNr = saleNr;
        this.saleDate = new Timestamp(System.currentTimeMillis());
    }

    public Sale(Long l, String saleNr) {
        this();
        this.saleId = l;
    }

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sale_id_seq")
    @SequenceGenerator(name = "sale_id_seq", sequenceName = "sale_sale_id_seq", allocationSize = 1)
    @Column(name = "SALE_ID", nullable = false)
    private Long saleId;


    @Setter
    @Getter
    @Column(name = "SALE_DATE", nullable = false)
    private Date saleDate;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INVOICE_ID", referencedColumnName = "INVOICE_ID")
    @JsonBackReference
    private Invoice invoice;

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
        invoice.addSale(this);
    }

    @Setter
    @Getter
    @Column(name = "SALE_NR", nullable = false)
    private String saleNr;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID")
    @JsonBackReference
    private Staff staff = null;

    public void setStaff(Staff staff) {
        this.staff = staff;
        staff.addSale(this);
    }

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
        Sale sale = (Sale) o;
        return Objects.equals(saleDate, sale.saleDate) && Objects.equals(saleNr, sale.saleNr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(saleDate, saleNr);
    }

    @Override
    public String toString() {
        return "Sale{" +
                "saleId=" + saleId +
                ", saleDate=" + saleDate +
                ", invoice=" + invoice +
                ", saleNr='" + saleNr + '\'' +
                ", staff=" + staff +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    public int compareTo(Sale sale) {
        int value = this.saleNr.compareTo(sale.getSaleNr());
        if (value == 0) {
            value = this.saleDate.compareTo(sale.getSaleDate());
        }
        return value;
    }
}