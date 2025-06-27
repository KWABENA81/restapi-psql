package asksef.entity;

import asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Entity
@Table(name = "CUSTOMER", schema = "rest_app")
public class Customer implements Serializable, Comparable<Customer> {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(Customer.class);

    public Customer() {
        this.lastUpdate = LocalDateTime.now();
    }

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "customer_customer_id_seq", allocationSize = 1)
    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;

    @Setter
    @Getter
    @Column(name = "FIRST_NAME", length = 45, nullable = false)
    private String firstName;

    @Setter
    @Getter
    @Column(name = "LAST_NAME", length = 45, nullable = false)
    private String lastName;

    //@CreationTimestamp
    @Column(name = "CREATE_DATE", nullable = false)
    private String createDate;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
    @JsonBackReference
    private Address address;

    public void setAddress(Address address) {
        this.address = address;
        address.addCustomer(this);
    }

    @Getter
    @Basic(optional = false)
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    @Convert(converter = DateConverter.class)
    private LocalDateTime lastUpdate;

    @Setter
    @OneToMany(targetEntity = Invoice.class, mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Invoice> invoiceList;

    public List<Invoice> getInvoiceList() {
        return (invoiceList != null) ? invoiceList : new ArrayList<>();
    }

    public void addInvoice(Invoice invoice) {
        List<Invoice> invoices = getInvoiceList();
        if (!invoices.contains(invoice)) {
            invoices.add(invoice);
        }
        invoice.setCustomer(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(firstName, customer.firstName)
                && Objects.equals(lastName, customer.lastName)
                && Objects.equals(createDate, customer.createDate)
                && Objects.equals(address, customer.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, createDate, address);
    }

    @Override
    public String toString() {
        return "Customer{" + "customerId=" + customerId +
                ", firstName='" + firstName + '\'' + ", lastName='" +
                lastName + '\'' + ", createDate='" + createDate + '\'' +
                ", address=" + address + ", lastUpdate=" + lastUpdate +
                //   ", invoiceList=" + invoiceList +
                '}';
    }

    @Override
    public int compareTo(Customer customer) {
        int value = this.lastName.compareTo(customer.getLastName());
        if (value == 0) {
            value = this.firstName.compareTo(customer.getFirstName());
        }
        if (value == 0) {
            value = this.address.compareTo(customer.getAddress());
        }
        return value;
    }
}