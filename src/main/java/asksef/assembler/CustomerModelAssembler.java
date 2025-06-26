package asksef.assembler;

import asksef.controller.CustomerController;
import asksef.entity.Customer;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NonNullApi
public class CustomerModelAssembler implements RepresentationModelAssembler<Customer, EntityModel<Customer>> {
    @Override
    public EntityModel<Customer> toModel(Customer customer) {
        try {
            return EntityModel.of(customer,
                    linkTo(methodOn(CustomerController.class).one(customer.getCustomerId())).withSelfRel(),
//                    linkTo(methodOn(CustomerController.class).add(customer)).withSelfRel(),
//                    linkTo(methodOn(CustomerController.class).delete(customer.getCustomerId())).withSelfRel(),
//                    linkTo(methodOn(CustomerController.class).update(customer.getCustomerId(), customer)).withSelfRel(),
                    linkTo(methodOn(CustomerController.class).all()).withRel("all"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
