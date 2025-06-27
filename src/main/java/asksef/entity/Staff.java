package asksef.entity;

import asksef.config.DateConverter;
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
@Table(name = "STAFF", schema = "rest_app")
public class Staff implements Serializable, Comparable<Staff> {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(Staff.class);


    public Staff() {
        this.lastUpdate = LocalDateTime.now();
    }

    public Staff(String firstName, String lastName, String username) {
        this();
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    public Staff(Long id, String firstName, String lastName, String username) {
        this();
        this.staffId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_id_seq")
    @SequenceGenerator(name = "staff_id_seq", sequenceName = "staff_staff_id_seq", allocationSize = 1)
    @Column(name = "STAFF_ID", nullable = false)
    private Long staffId;

    @Setter
    @Getter
    @Column(name = "FIRST_NAME", length = 45, nullable = false)
    private String firstName;

    @Setter
    @Getter
    @Column(name = "LAST_NAME", length = 45, nullable = false)
    private String lastName;

//    @Getter
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
//    @JsonBackReference
//    private Address address;
//
//    public void setAddress(Address address) {
//        this.address = address;
//        address.addStaff(this);
//    }

    @Setter
    @Getter
    @Column(name = "USERNAME", nullable = false, length = 16)
    private String username;

    @Convert(converter = DateConverter.class)
    private LocalDateTime lastUpdate;

    @Basic(optional = false)
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }


    @Setter
    @OneToMany(targetEntity = Payment.class, mappedBy = "staff", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Payment> paymentList;

    public List<Payment> getPaymentList() {
        return (paymentList != null) ? paymentList : new ArrayList<>();
    }

    public void addPayment(Payment payment) {
        List<Payment> payments = getPaymentList();
        if (!payments.contains(payment)) {
            payments.add(payment);
        }
        payment.setStaff(this);
    }

    @Setter
    @OneToMany(targetEntity = Sale.class, mappedBy = "staff", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Sale> saleList;

    public List<Sale> getSaleList() {
        return (saleList != null) ? saleList : new ArrayList<>();
    }

    public void addSale(Sale sale) {
        List<Sale> sales = getSaleList();
        if (!sales.contains(sale))
            sales.add(sale);
        sale.setStaff(this);
    }

    @Setter
    @OneToMany(targetEntity = Store.class, mappedBy = "staff", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Store> storeList;

    public List<Store> getStoreList() {
        return (storeList != null) ? storeList : new ArrayList<>();
    }

    public void addStore(Store store) {
        List<Store> storeList1 = getStoreList();
        if (!storeList1.contains(store)) {
            storeList1.add(store);
        }
        store.setStaff(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Staff staff = (Staff) o;
        return Objects.equals(firstName, staff.firstName)
                && Objects.equals(lastName, staff.lastName)
                && Objects.equals(username, staff.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, username);
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staffId=" + staffId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", lastUpdate=" + lastUpdate +
                //    ", paymentList=" + paymentList +
                //   ", saleList=" + saleList +
                // ", storeList=" + storeList +
                '}';
    }

    @Override
    public int compareTo(Staff staff) {
        int value = this.firstName.compareTo(staff.getFirstName());
        if (value == 0) {
            value = this.lastName.compareTo(staff.getUsername());
        }
        return value;
    }
}