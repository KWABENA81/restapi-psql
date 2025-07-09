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
import org.springframework.hateoas.CollectionModel;
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
    private final CustomerModelAssemblerSupport assemblerSupport;

    public CustomerController(CustomerService customerService, CustomerRepository customerRepository,
                              CustomerModelAssemblerSupport assemblerSupport) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.assemblerSupport = assemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<CustomerModel>> all() {
        List<Customer> entityList = customerService.findAll().stream().toList();
        return new ResponseEntity<>(this.assemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<CustomerModel> one(@PathVariable("id") Long id) {
        return this.customerRepository.findById(id)
                .map(assemblerSupport::toModel)
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
        @NonNull CustomerModel entityModel = assemblerSupport.toModel(savedCust);
        log.info("CustomerController: addCustomer");
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Customer newCustomer) {
        Customer updatedCcustomer = customerService.update(id, newCustomer);
        @NonNull CustomerModel entityModel = this.assemblerSupport.toModel(updatedCcustomer);
        log.info("CustomerController: updateCustomer");
        return ResponseEntity
                .created(linkTo(methodOn(CustomerController.class).one(updatedCcustomer.getCustomerId()))
                        .toUri()).body(entityModel);
    }

    @GetMapping(value = "/{id}/address", produces = "application/hal+json")
    public ResponseEntity<AddressModel> findAddressOfCustomer(@PathVariable("id") Long id) {
        Address address = this.customerService.findAddressOfCustomer(id);
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

}
