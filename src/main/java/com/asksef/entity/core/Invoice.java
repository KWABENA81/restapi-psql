package com.asksef.entity.core;

import com.asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "INVOICE", schema = "rest_app")
public class Invoice implements Serializable, Comparable<Invoice> {
    @Serial
    private static final long serialVersionUID = 1L;

    public Invoice() {
        this.lastUpdate = LocalDateTime.now();
        this.invoiceNr = String.valueOf(Math.abs(LocalDateTime.now().hashCode()));
    }

    @Builder
    public Invoice(String invoiceNr, Customer customer, Order order, LocalDateTime lastUpdate, Long id) {
        this.invoiceId = id;
        this.invoiceNr = invoiceNr;
        this.customer = customer;
        this.order = order;
        this.lastUpdate = lastUpdate;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "invoice_id_seq")
    @SequenceGenerator(name = "invoice_id_seq", sequenceName = "rest_app.invoice_invoice_id_seq", allocationSize = 1)
    @Column(name = "INVOICE_ID", nullable = false)
    private Long invoiceId;

    @Setter
    @Getter
    @EqualsAndHashCode.Include
    @Column(name = "INVOICE_NR", unique = true)
    private String invoiceNr;

    @Getter
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @JsonBackReference
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.addInvoice(this);
    }

    @Getter
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "ORDER_ID")
    @JsonBackReference
    private Order order;

    public void setOrder(Order order) {
        this.order = order;
        order.addInvoice(this);
    }

//
//    @Setter
//    @OneToMany(targetEntity = Order.class, mappedBy = "invoice", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    @JsonIgnore
//    private List<Order> orderList;
//
//    public List<Order> getOrderList() {
//        return (orderList != null) ? orderList : new ArrayList<>();
//    }
//
//    public void addOrder(Order order) {
//        List<Order> orders = getOrderList();
//        if (!orders.contains(order)) {
//            orders.add(order);
//        }
//        order.setInvoice(this);
//    }

    @Setter
    @ToString.Exclude
    @OneToMany(targetEntity = Payment.class, mappedBy = "invoice", cascade = CascadeType.ALL)
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
        payment.setInvoice(this);
    }

    @Convert(converter = DateConverter.class)
    private LocalDateTime lastUpdate;

    @Basic(optional = false)
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        Invoice invoice = (Invoice) o;
//        return Objects.equals(invoiceNr, invoice.invoiceNr) && Objects.equals(customer, invoice.customer);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(invoiceNr, customer);
//    }
//
//    @Override
//    public String toString() {
//        return "Invoice{" +
//                "invoiceId=" + invoiceId +
//                ", invoiceNr=" + invoiceNr +
//                ", customer=" + customer +
//                //       ", orderList=" + orderList +
//                //     ", paymentList=" + paymentList +
//                ", lastUpdate=" + lastUpdate +
//                '}';
//    }


    @Override
    public int compareTo(Invoice inv) {
        int value = this.invoiceNr.compareTo(inv.getInvoiceNr());
        if (value != 0) {
            return value;
        } else {
            return this.customer.compareTo(inv.getCustomer());
        }
    }

//    @OneToMany(targetEntity = Item.class, mappedBy = "ITEM", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    private List<Item> itemList;
//
//    public List<Item> getItemList() {
//        return (itemList != null) ? itemList : new ArrayList<>();
//    }
//
//    public void setItemList(List<Item> items) {
//        this.itemList = items;
//    }
//
//    public void addItem(Item item) {
//        List<Item> items = getItemList();
//        if (!items.contains(item)) items.add(item);
//        item.setInvoice(this);
//    }
}


