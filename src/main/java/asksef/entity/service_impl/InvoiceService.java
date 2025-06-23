package asksef.entity.service_impl;


import asksef.entity.Invoice;
import asksef.entity.repository.InvoiceRepository;
import asksef.entity.service_interface.InvoiceServiceInterface;
import asksef.errors.CustomResourceExistsException;
import asksef.errors.CustomResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
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

    @Override
    public Invoice findById(Long id) {
        Invoice invoice = this.invoiceRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Invoice", "id", null, id)
        );
        log.info("Invoice Service:  findById");
        return invoice;
    }

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

            invoice1.setPaymentList(invoice.getPaymentList());
            invoice1.setSaleList(invoice.getSaleList());
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
//            invoice1.setSaleList(invoice.getSaleList());
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
}
