package com.asksef.entity.core;

import com.asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Setter
@Entity
@Table(name = "ORDER", schema = "rest_app")
public class Order implements Serializable, Comparable<Order> {
    @Serial
    private static final long serialVersionUID = 1L;

    public Order() {
        this.lastUpdate = LocalDateTime.now();
        this.orderNr = String.valueOf(Math.abs(LocalDateTime.now().hashCode()));
        this.orderDate = new Timestamp(System.currentTimeMillis());
    }

    @Builder
    public Order(Long orderId, Date orderDate, LocalDateTime lastUpdate, Item item, Staff staff, String orderNr) {
        this.orderId = orderId;
        this.orderNr = orderNr;
        this.orderDate = orderDate;
        //  this.invoice = invoice;
        this.staff = staff;
        this.item = item;
        this.lastUpdate = lastUpdate;
    }

//    public Order(Long l, String orderNr) {
//        this();
//        this.orderId = l;
//    }

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_id_seq")
    @SequenceGenerator(name = "order_id_seq", sequenceName = "rest_app.order_order_id_seq", allocationSize = 1)
    @Column(name = "ORDER_ID", nullable = false)
    private Long orderId;

    @Setter
    @Getter
    @Column(name = "ORDER_DATE", nullable = false)
    private Date orderDate;

    @Setter
    @OneToMany(targetEntity = Invoice.class, mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<Invoice> invoiceList;

    public List<Invoice> getInvoiceList() {
        return (invoiceList != null) ? invoiceList : new ArrayList<>();
    }

    public void addInvoice(Invoice invoice) {
        List<Invoice> invoices = this.getInvoiceList();
        if (!invoices.contains(invoice)) {
            invoices.add(invoice);
        }
        invoice.setOrder(this);
    }

    @Setter
    @Getter
    @Column(name = "ORDER_NR", nullable = false)
    private String orderNr;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID")
    @JsonBackReference
    private Staff staff = null;

    public void setStaff(Staff staff) {
        this.staff = staff;
        staff.addOrder(this);
    }

    @Getter
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
    @JsonBackReference
    private Item item = null;

    public void setItem(Item item) {
        this.item = item;
        item.addOrder(this);
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
        Order order = (Order) o;
        return Objects.equals(orderDate, order.orderDate) && Objects.equals(orderNr, order.orderNr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderDate, orderNr);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderDate=" + orderDate +
                ", orderNr='" + orderNr + '\'' +
                ", staff=" + staff +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    public int compareTo(Order order) {
        int value = this.orderNr.compareTo(order.getOrderNr());
        if (value == 0) {
            value = this.orderDate.compareTo(order.getOrderDate());
        }
        return value;
    }
}