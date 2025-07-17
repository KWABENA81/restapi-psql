package com.asksef.entity.service_impl;


import com.asksef.entity.core.Customer;
import com.asksef.entity.core.Invoice;
import com.asksef.entity.core.Order;
import com.asksef.entity.core.Payment;
import com.asksef.entity.model.InvoiceModel;
import com.asksef.entity.repository.InvoiceRepository;
import com.asksef.entity.service_interface.InvoiceServiceInterface;
import com.asksef.errors.CustomResourceExistsException;
import com.asksef.errors.CustomResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class InvoiceService implements InvoiceServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(InvoiceService.class);
    private final InvoiceRepository invoiceRepository;

    public InvoiceService(InvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Collection<Invoice> findAll() {
        log.info("Invoice Service: findAll");
        return this.invoiceRepository.findAll();
    }

    @Override
    public Collection<Invoice> findAll(int pageNumber, int pageSize) {
        return this.invoiceRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    public Page<Invoice> findAll(Pageable pageable) {
        return this.invoiceRepository.findAll(pageable);
    }

    @Override
    public Invoice findById(Long id) {
        Invoice invoice = this.invoiceRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Invoice", "id", null, id)
        );
        log.info("Invoice Service:  findById");
        return invoice;
    }

    @Transactional
    public Invoice save(@Valid InvoiceModel invoiceModel) {
        Invoice invoice = Invoice.builder()
                .invoiceNr(invoiceModel.getInvoiceNr())
                .customer(invoiceModel.getCustomer())
                .lastUpdate(invoiceModel.getLastUpdate())
                .build();
        return save(invoice);
    }

    @Transactional
    @Override
    public Invoice save(Invoice invoice) {
        Optional<Invoice> optional = this.invoiceRepository.findById(invoice.getInvoiceId());
        if (optional.isEmpty()) {
            return this.invoiceRepository.save(invoice);
        } else {
            throw new CustomResourceExistsException("Invoice", "id", null, invoice.getInvoiceId());
        }
    }

    @Override
    public Invoice update(Invoice invoice) {
        Long id = invoice.getInvoiceId();
        Optional<Invoice> optional = this.invoiceRepository.findById(id);

        if (optional.isPresent()) {
            Invoice invoice1 = optional.get();
            invoice1.setInvoiceNr(invoice.getInvoiceNr());
            invoice1.setCustomer(invoice.getCustomer());
            invoice1.setOrder(invoice.getOrder());

            invoice1.setPaymentList(invoice.getPaymentList());
            log.info("Invoice Service:  update");
            return invoiceRepository.save(invoice1);
        } else {
            throw new CustomResourceNotFoundException("Invoice", "id", null, id);
        }
    }

    public Invoice update(Long id, Invoice invoice) {
        if (Objects.equals(id, invoice.getInvoiceId())) {
            return update(invoice);
        } else {
            throw new CustomResourceNotFoundException("Invoice", "id", null, id);
        }
    }

    //            invoice1.setInvoiceNr(invoice.getInvoiceNr());
//            invoice1.setCustomer(invoice.getCustomer());
//
//            invoice1.setPaymentList(invoice.getPaymentList());
//            invoice1.setOrderList(invoice.getOrderList());
//            log.info("Invoice Service:  update");
//            return invoiceRepository.save(invoice1);
//        }
//        return null;
    @Override
    public void delete(Invoice invoice) {
        delete(invoice.getInvoiceId());
    }

    public void delete(Long id) {
        Invoice invoice = invoiceRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Invoice", "id", null, id)
        );
        this.invoiceRepository.delete(invoice);
    }

    @Override
    public Long count() {
        return this.invoiceRepository.count();
    }

    public Customer findCustomerOnInvoice(Long id) {
        return this.invoiceRepository.findCustomerOnInvoice(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Customer", "id", null, id)
        );
    }

    public Order findInvoiceOrder(Long id) {
        return this.invoiceRepository.findOrderInvoice(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Order", "id", null, id)
        );
    }


    public List<Payment> findInvoicePayments(Long id) {
        Invoice invoice = this.invoiceRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Invoice", "id", null, id)
        );
        return invoice.getPaymentList();
    }
}
