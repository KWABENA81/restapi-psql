package asksef.controller;

import asksef.assembler.CustomerModelAssembler;
import asksef.entity.Customer;
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

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Customer>> all() {
        List<EntityModel<Customer>> entityModelList = customerService.findAll().
                stream().map(customerModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(CustomerController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Customer> one(@PathVariable("id") Long id) {
        Customer customer = customerService.findById(id);
        return customerModelAssembler.toModel(customer);
    }


    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.customerService.delete(id);
        return new ResponseEntity<>("Customer entity deleted", HttpStatus.NO_CONTENT);
    }

    /// ///////////////////

    @PostMapping("/add")
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

    @PutMapping("/update/{id}")
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
