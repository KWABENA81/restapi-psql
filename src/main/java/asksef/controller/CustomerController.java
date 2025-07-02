package asksef.controller;

import asksef.assembler.CustomerModelAssembler;
import asksef.entity.Address;
import asksef.entity.Customer;
import asksef.entity.entity_model.AddressModel;
import asksef.entity.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/customer")
public class CustomerController {
    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);
    private final CustomerService customerService;
    private final CustomerModelAssembler customerModelAssembler;


    public CustomerController(CustomerService customerService, CustomerModelAssembler customerModelAssembler) {
        this.customerService = customerService;
        this.customerModelAssembler = customerModelAssembler;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Customer>> all() {
        List<EntityModel<Customer>> entityModelList = customerService.findAll().
                stream().map(customerModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(CustomerController.class).all()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Customer> one(@PathVariable("id") Long id) {
        Customer customer = customerService.findById(id);
        return customerModelAssembler.toModel(customer);
    }

    @GetMapping(value = "/{id}/address", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AddressModel> findAddressOfCustomer(@PathVariable("id") Long id) {
        Address address = this.customerService.findAddressOfCustomer(id);
        //  build addree model
        AddressModel addressModel = AddressModel.builder()
                .addressId(address.getAddressId())
                .phone(address.getPhone())
                .city(address.getCity())
                .gpsCode(address.getGpsCode())
                .lastUpdate(address.getLastUpdate())
                .customerList(address.getCustomerList())
                .storeList(address.getStoreList())
                .build();
        addressModel.add(linkTo(methodOn(CustomerController.class)
                .findAddressOfCustomer(id)).withSelfRel());
        addressModel.add(linkTo(methodOn(CustomerController.class).one(id)).withRel("customer"));
        return new ResponseEntity<>(addressModel,HttpStatus.OK);
    }


    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.customerService.delete(id);
        return new ResponseEntity<>("Customer entity deleted", HttpStatus.NO_CONTENT);
    }

    /// ///////////////////

    @PostMapping(value = "/add", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(@RequestBody Customer customer) {
        Customer savedCust = customerService.save(customer);
        EntityModel<Customer> entityModel
                = customerModelAssembler.toModel(savedCust);
        log.info("CustomerController: addCustomer");
        //return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(CustomerController.class).one(savedCust.getCustomerId()))
                        .toUri()).body(entityModel);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Customer newCustomer) {
        Customer updatedCcustomer = customerService.update(id, newCustomer);
        EntityModel<Customer> entityModel = customerModelAssembler.toModel(updatedCcustomer);
        log.info("CustomerController: updateCustomer");
//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(CustomerController.class).one(updatedCcustomer.getCustomerId()))
                        .toUri()).body(entityModel);
    }

}
