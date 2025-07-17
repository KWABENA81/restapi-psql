package com.asksef.entity.core;

import com.asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "CUSTOMER", schema = "rest_app")
public class Customer implements Serializable, Comparable<Customer> {
    @Serial
    private static final long serialVersionUID = 1L;

    public Customer() {
        this.lastUpdate = LocalDateTime.now();
    }

    @Builder
    public Customer(String firstName, String lastName, Address address,
                    LocalDateTime createDate, LocalDateTime lastUpdate, Long id) {
        this.customerId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.createDate = createDate;
        this.lastUpdate = lastUpdate;
    }

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "rest_app.customer_customer_id_seq", allocationSize = 1)
    @Column(name = "CUSTOMER_ID", nullable = false)
    private Long customerId;

    @Setter
    @Getter
    @EqualsAndHashCode.Include
    @Column(name = "FIRST_NAME", length = 45, nullable = false)
    private String firstName;

    @Setter
    @Getter
    @EqualsAndHashCode.Include
    @Column(name = "LAST_NAME", length = 45, nullable = false)
    private String lastName;

    @Setter
    @Getter
    @EqualsAndHashCode.Include
    @CreationTimestamp
    @Column(name = "CREATE_DATE", nullable = false)
    private LocalDateTime createDate;

    @Getter
    @ManyToOne(fetch = FetchType.EAGER)
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
    @ToString.Exclude
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
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        Customer customer = (Customer) o;
//        return Objects.equals(firstName, customer.firstName)
//                && Objects.equals(lastName, customer.lastName)
//                && Objects.equals(createDate, customer.createDate)
//                && Objects.equals(address, customer.address);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(firstName, lastName, createDate, address);
//    }
//
//    @Override
//    public String toString() {
//        return "Customer{" + "customerId=" + customerId +
//                ", firstName='" + firstName + '\'' + ", lastName='" +
//                lastName + '\'' + ", createDate='" + createDate + '\'' +
//                ", address=" + address + ", lastUpdate=" + lastUpdate + //", invoiceList=" + invoiceList +
//                '}';
//    }

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