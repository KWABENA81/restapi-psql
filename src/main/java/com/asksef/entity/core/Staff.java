package com.asksef.entity.core;

import com.asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
@Table(name = "STAFF", schema = "rest_app")
public class Staff implements Serializable, Comparable<Staff> {
    @Serial
    private static final long serialVersionUID = 1L;

    public Staff() {
        this.lastUpdate = LocalDateTime.now();
    }

    @Builder
    public Staff(String firstName, String lastName, String username, LocalDateTime lastUpdate, Long id) {
        this.staffId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.lastUpdate = lastUpdate;
    }

    public Staff(Long staffId, String firstName, String lastName, String username) {
        this();
        this.staffId = staffId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "staff_id_seq")
    @SequenceGenerator(name = "staff_id_seq", sequenceName = "rest_app.staff_staff_id_seq", allocationSize = 1)
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
    @ToString.Exclude
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
    @ToString.Exclude
    @OneToMany(targetEntity = Order.class, mappedBy = "staff", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Order> orderList;

    public List<Order> getOrderList() {
        return (orderList != null) ? orderList : new ArrayList<>();
    }

    public void addOrder(Order order) {
        List<Order> orders = getOrderList();
        if (!orders.contains(order))
            orders.add(order);
        order.setStaff(this);
    }
//
//    @Getter
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
//    @JsonBackReference
//    private Address address;
//
//    public void setAddress(Address address) {
//        this.address = address;
//        address.addStaff(this);
//    }

    @Setter
    @ToString.Exclude
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
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        Staff staff = (Staff) o;
//        return Objects.equals(firstName, staff.firstName)
//                && Objects.equals(lastName, staff.lastName)
//                && Objects.equals(username, staff.username);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(firstName, lastName, username);
//    }
//
//    @Override
//    public String toString() {
//        return "Staff{" +
//                "staffId=" + staffId +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", username='" + username + '\'' +
//                ", lastUpdate=" + lastUpdate +
//                //    ", paymentList=" + paymentList +
//                //   ", orderList=" + orderList +
//                // ", storeList=" + storeList +
//                '}';
//    }

    @Override
    public int compareTo(Staff staff) {
        int value = this.firstName.compareTo(staff.getFirstName());
        if (value == 0) {
            value = this.lastName.compareTo(staff.getUsername());
        }
        return value;
    }
}