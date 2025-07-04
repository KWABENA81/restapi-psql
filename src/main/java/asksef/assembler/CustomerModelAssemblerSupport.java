package asksef.assembler;

import asksef.controller.CustomerController;
import asksef.entity.Customer;
import asksef.entity.entity_model.CustomerModel;
import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CustomerModelAssemblerSupport extends RepresentationModelAssemblerSupport<Customer, CustomerModel> {
    public CustomerModelAssemblerSupport() {
        super(CustomerController.class, CustomerModel.class);
    }

    @NonNull
    @Override
    public CustomerModel toModel(@NonNull Customer entity) {
        CustomerModel model = instantiateModel(entity);

        model.add(linkTo(methodOn(CustomerController.class).one(entity.getCustomerId())).withRel("Id Link"));
        model.add(
                linkTo(methodOn(CustomerController.class).findAddressOfCustomer(entity.getCustomerId()))
                        .withRel("address Of Customer"));
        model.add(linkTo(methodOn(CustomerController.class).all()).withRel("all"));
        model.add(linkTo(methodOn(CustomerController.class).add(CustomerModel.builder().build())).withRel("create"));
        model.add(linkTo(methodOn(CustomerController.class)
                .delete(entity.getCustomerId())).withRel("delete Customer"));
        model.add(linkTo(methodOn(CustomerController.class)
                .update(entity.getCustomerId(), Customer.builder().build())).withRel("update Customer"));

        model.setCustomerId(entity.getCustomerId());
        model.setFirstName(entity.getFirstName());
        model.setLastName(entity.getLastName());
        model.setAddress(entity.getAddress());
        model.setLastUpdate(entity.getLastUpdate());
        // model.setInvoiceModels(toInvoiceCollectionModel(entity.getInvoiceList()));
        return model;
    }


    @NonNull
    @Override
    public CollectionModel<CustomerModel> toCollectionModel(@NonNull Iterable<? extends Customer> entities) {
        CollectionModel<CustomerModel> models = super.toCollectionModel(entities);
        models.add(linkTo(methodOn(CustomerController.class).all()).withSelfRel());
        return models;
    }
}
