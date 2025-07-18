package com.asksef.controller;

import com.asksef.assembler.AddressModelAssemblerSupport;
import com.asksef.assembler.CustomerModelAssemblerSupport;
import com.asksef.assembler.InvoiceModelAssemblerSupport;
import com.asksef.entity.core.Address;
import com.asksef.entity.core.Customer;
import com.asksef.entity.core.Invoice;
import com.asksef.entity.model.AddressModel;
import com.asksef.entity.model.CustomerModel;
import com.asksef.entity.model.InvoiceModel;
import com.asksef.entity.repository.CustomerRepository;
import com.asksef.entity.service_impl.CustomerService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api/customer")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerRepository customerRepository;
    private final PagedResourcesAssembler<Customer> pagedResourcesAssembler;
    private final CustomerModelAssemblerSupport customerModelAssemblerSupport;

    public CustomerController(CustomerService customerService, CustomerRepository customerRepository,
                              PagedResourcesAssembler<Customer> pagedResourcesAssembler,
                              CustomerModelAssemblerSupport assemblerSupport) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.customerModelAssemblerSupport = assemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<CustomerModel>> all() {
        List<Customer> entityList = customerService.findAll().stream().toList();
        return new ResponseEntity<>(this.customerModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<CustomerModel> one(@PathVariable("id") Long id) {
        return this.customerRepository.findById(id)
                .map(customerModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.customerService.delete(id);
        return new ResponseEntity<>("Customer entity deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    public ResponseEntity<CustomerModel> add(@RequestBody CustomerModel customerModel) {
        Customer savedCust = customerService.save(customerModel);
        @NonNull CustomerModel entityModel = customerModelAssemblerSupport.toModel(savedCust);
        log.info("CustomerController: addCustomer");
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Customer newCustomer) {
        Customer updatedCcustomer = customerService.update(id, newCustomer);
        @NonNull CustomerModel entityModel = this.customerModelAssemblerSupport.toModel(updatedCcustomer);
        log.info("CustomerController: updateCustomer");
        return ResponseEntity
                .created(linkTo(methodOn(CustomerController.class).one(updatedCcustomer.getCustomerId()))
                        .toUri()).body(entityModel);
    }

    @GetMapping(value = "/{id}/address", produces = "application/hal+json")
    public ResponseEntity<AddressModel> findCustomerAddress(@PathVariable("id") Long id) {
        Address address = this.customerService.findCustomerAddress(id);
        //  build address model
        AddressModel model = new AddressModelAssemblerSupport().toModel(address);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/invoices", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<InvoiceModel>> findCustomerInvoices(@PathVariable("id") Long id) {
        List<Invoice> invoiceList = customerService.findCustomerInvoices(id);
        CollectionModel<InvoiceModel> invoiceModels = new InvoiceModelAssemblerSupport().toCollectionModel(invoiceList);
        return new ResponseEntity<>(invoiceModels, HttpStatus.OK);
    }

    @GetMapping(value = "/paged")
    public ResponseEntity<PagedModel<CustomerModel>> paged(Pageable pageable) {
        Page<Customer> entityPage = customerService.findAll(pageable);

        PagedModel<CustomerModel> pagedModel = pagedResourcesAssembler.toModel(entityPage, customerModelAssemblerSupport);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }
}
