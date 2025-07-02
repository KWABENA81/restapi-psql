package asksef.assembler_support;

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

        model.add(linkTo(methodOn(CustomerController.class).one(entity.getCustomerId())).withSelfRel());
        model.add(
                linkTo(methodOn(CustomerController.class).findAddressOfCustomer(entity.getCustomerId()))
                        .withRel("addressOfCustomer"));
        model.add(linkTo(methodOn(CustomerController.class).all()).withRel("all"));

        model.setCustomerId(entity.getCustomerId());
        model.setFirstName(entity.getFirstName());
        model.setLastName(entity.getLastName());
        model.setAddress(entity.getAddress());
        model.setLastUpdate(entity.getLastUpdate());
        // model.setInvoiceModels(toInvoiceCollectionModel(entity.getInvoiceList()));
        return model;
    }

//    private CollectionModel<InvoiceModel> toInvoiceCollectionModel(List<Invoice> invoiceList) {
//        InvoiceModelAssemblerSupport sup = new InvoiceModelAssemblerSupport();
//        Object collection = sup.toCollectionModel(invoiceList);
//        return sup;
//    }

    @NonNull
    @Override
    public CollectionModel<CustomerModel> toCollectionModel(@NonNull Iterable<? extends Customer> entities) {
        CollectionModel<CustomerModel> models = super.toCollectionModel(entities);
        models.add(linkTo(methodOn(CustomerController.class).all()).withSelfRel());
        return models;
    }
}
