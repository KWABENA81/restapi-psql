package com.asksef.controller;

import com.asksef.assembler.CustomerModelAssemblerSupport;
import com.asksef.assembler.InvoiceModelAssemblerSupport;
import com.asksef.assembler.OrderModelAssemblerSupport;
import com.asksef.assembler.PaymentModelAssemblerSupport;
import com.asksef.entity.core.Customer;
import com.asksef.entity.core.Invoice;
import com.asksef.entity.core.Order;
import com.asksef.entity.core.Payment;
import com.asksef.entity.model.CustomerModel;
import com.asksef.entity.model.InvoiceModel;
import com.asksef.entity.model.OrderModel;
import com.asksef.entity.model.PaymentModel;
import com.asksef.entity.repository.InvoiceRepository;
import com.asksef.entity.service_impl.InvoiceService;
import jakarta.servlet.ServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api/invoice")
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final InvoiceRepository invoiceRepository;
    private final InvoiceModelAssemblerSupport invoiceModelAssemblerSupport;

    public InvoiceController(InvoiceService invoiceService, InvoiceRepository invoiceRepository,
                             InvoiceModelAssemblerSupport invoiceModelAssemblerSupport) {
        this.invoiceService = invoiceService;
        this.invoiceRepository = invoiceRepository;
        this.invoiceModelAssemblerSupport = invoiceModelAssemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<InvoiceModel>> all() {
        List<Invoice> entityList = this.invoiceService.findAll().
                stream().toList();
        return new ResponseEntity<>(this.invoiceModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<InvoiceModel> one(@PathVariable("id") Long id) {
        return this.invoiceRepository.findById(id)
                .map(invoiceModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.invoiceService.delete(id);
        return new ResponseEntity<>("Invoice entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<InvoiceModel> add(@RequestBody InvoiceModel invoiceModel) {
        Invoice savedInvoice = this.invoiceService.save(invoiceModel);
        @NonNull InvoiceModel entityModel = invoiceModelAssemblerSupport.toModel(savedInvoice);
        log.info("Invoice addInvoice");
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Invoice newInvoice, ServletResponse servletResponse) {
        Invoice updateInvoice = this.invoiceService.update(id, newInvoice);

        @NonNull InvoiceModel entityModel = invoiceModelAssemblerSupport.toModel(updateInvoice);
        log.info("Invoice updated");
//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(InvoiceController.class).one(id)).withSelfRel()
                        .toUri()).body(entityModel);
    }

    @GetMapping(value = "{id}/customer", produces = "application/hal+json")
    public ResponseEntity<CustomerModel> findCustomerOnInvoice(@PathVariable("id") Long id) {
        Customer customer = this.invoiceService.findCustomerOnInvoice(id);
        @NonNull CustomerModel customerModel = new CustomerModelAssemblerSupport().toModel(customer);
        customerModel.add(linkTo(methodOn(InvoiceController.class).findCustomerOnInvoice(id)).withRel("Customer On Invoice"));
        return new ResponseEntity<>(customerModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/order", produces = "application/hal+json")
    public ResponseEntity<OrderModel> findInvoiceOrder(@PathVariable("id") Long id) {
        Order order = invoiceService.findInvoiceOrder(id);
        @NonNull OrderModel orderModel = new OrderModelAssemblerSupport().toModel(order);

        orderModel.add(linkTo(methodOn(InvoiceController.class).findInvoiceOrder(id)).withRel("Order Invoice"));
        return new ResponseEntity<>(orderModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/payments", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<PaymentModel>> findInvoicePayments(@PathVariable("id") Long id) {
        List<Payment> paymentList = invoiceService.findInvoicePayments(id);
        CollectionModel<PaymentModel> paymentModels = new PaymentModelAssemblerSupport().toCollectionModel(paymentList);
        return new ResponseEntity<>(paymentModels, HttpStatus.OK);
    }
}
