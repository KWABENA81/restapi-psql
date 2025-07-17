package com.asksef.assembler;

import com.asksef.controller.CustomerController;
import com.asksef.entity.core.Customer;
import com.asksef.entity.core.Invoice;
import com.asksef.entity.model.CustomerModel;
import com.asksef.entity.model.InvoiceModel;
import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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

        model.add(linkTo(methodOn(CustomerController.class).all()).withRel("all"));
        model.add(linkTo(methodOn(CustomerController.class).add(CustomerModel.builder().build())).withRel("Create"));
        model.add(linkTo(methodOn(CustomerController.class)
                .delete(entity.getCustomerId())).withRel("Delete Customer"));
        model.add(linkTo(methodOn(CustomerController.class)
                .update(entity.getCustomerId(), Customer.builder().build())).withRel("Update Customer"));
        model.add(linkTo(methodOn(CustomerController.class)
                .findCustomerAddress(entity.getCustomerId())).withRel("Customer Address"));
        model.add(linkTo(methodOn(CustomerController.class)
                .findCustomerInvoices(entity.getCustomerId())).withRel("Customer Invoices"));
        model.add(linkTo(methodOn(CustomerController.class).one(entity.getCustomerId())).withSelfRel());

        model.setFirstName(entity.getFirstName());
        model.setLastName(entity.getLastName());
        model.setAddress(entity.getAddress());
        model.setLastUpdate(entity.getLastUpdate());
        model.setInvoiceModelList(toInvoiceCollectionModel(entity.getInvoiceList()));
        return model;
    }

    private List<InvoiceModel> toInvoiceCollectionModel(List<Invoice> invoiceList) {
        if (invoiceList == null || invoiceList.isEmpty()) {
            return Collections.emptyList();
        }
        return invoiceList.stream()
                .map(inv -> InvoiceModel.builder()
                        .invoiceNr(inv.getInvoiceNr())
                        .customer(inv.getCustomer())
                        .lastUpdate(inv.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(CustomerController.class)
                                .one(inv.getInvoiceId()))
                                .withSelfRel())).toList();
    }


    @NonNull
    @Override
    public CollectionModel<CustomerModel> toCollectionModel(@NonNull Iterable<? extends Customer> entities) {
        CollectionModel<CustomerModel> models = super.toCollectionModel(entities);
        models.add(linkTo(methodOn(CustomerController.class).all()).withSelfRel());
        return models;
    }
}
