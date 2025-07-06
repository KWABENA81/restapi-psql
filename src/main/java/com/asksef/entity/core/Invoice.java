package com.asksef.entity.core;

import com.asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
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
@Table(name = "INVOICE", schema = "rest_app")
public class Invoice implements Serializable, Comparable<Invoice> {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(Invoice.class);

    public Invoice() {
        this.lastUpdate = LocalDateTime.now();
        this.invoiceNr = String.valueOf(Math.abs(LocalDateTime.now().hashCode()));
    }

    @Builder
    public Invoice(Long invoiceId, String invoiceNr, Customer customer, LocalDateTime lastUpdate) {
        this.invoiceId = invoiceId;
        this.invoiceNr = invoiceNr;
        this.customer = customer;
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
    @Column(name = "INVOICE_NR")
    private String invoiceNr;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CUSTOMER_ID", referencedColumnName = "CUSTOMER_ID")
    @JsonBackReference
    private Customer customer;

    public void setCustomer(Customer customer) {
        this.customer = customer;
        customer.addInvoice(this);
    }

    @Setter
    @OneToMany(targetEntity = Sale.class, mappedBy = "invoice", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Sale> saleList;

    public List<Sale> getSaleList() {
        return (saleList != null) ? saleList : new ArrayList<>();
    }

    public void addSale(Sale sale) {
        List<Sale> sales = getSaleList();
        if (!sales.contains(sale)) {
            sales.add(sale);
        }
        sale.setInvoice(this);
    }

    @Setter
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Invoice invoice = (Invoice) o;
        return Objects.equals(invoiceNr, invoice.invoiceNr) && Objects.equals(customer, invoice.customer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceNr, customer);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId=" + invoiceId +
                ", invoiceNr=" + invoiceNr +
                ", customer=" + customer +
                //       ", saleList=" + saleList +
                //     ", paymentList=" + paymentList +
                ", lastUpdate=" + lastUpdate +
                '}';
    }


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


