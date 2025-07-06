package com.asksef.assembler;

import com.asksef.controller.InvoiceController;
import com.asksef.entity.core.Invoice;
import com.asksef.entity.model.InvoiceModel;
import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Component
public class InvoiceModelAssemblerSupport extends RepresentationModelAssemblerSupport<Invoice, InvoiceModel> {

    public InvoiceModelAssemblerSupport() {
        super(InvoiceController.class, InvoiceModel.class);
    }

    @NonNull
    @Override
    public InvoiceModel toModel(@NonNull Invoice entity) {
        InvoiceModel invoiceModel = instantiateModel(entity);
        invoiceModel.add(linkTo(methodOn(InvoiceController.class).all()).withRel("all"));
        invoiceModel.add(linkTo(methodOn(InvoiceController.class).one(entity.getInvoiceId())).withRel("one"));
       //  invoiceModel.add(linkTo(methodOn(InvoiceController.class).add(entity)).withRel("add"));
         invoiceModel.add(linkTo(methodOn(InvoiceController.class).findCustomerOnInvoice(entity.getInvoiceId())).withRel("Customer On Invoice"));
        //invoiceModel.add(linkTo(methodOn(InvoiceController.class).))

        invoiceModel.setInvoiceId(entity.getInvoiceId());
        invoiceModel.setInvoiceNr(entity.getInvoiceNr());
        invoiceModel.setCustomer(entity.getCustomer());
        invoiceModel.setLastUpdate(entity.getLastUpdate());
        //invoiceModel.setSaleModels(entity.getSaleList());
        //invoiceModel.setPaymentModels(entity.getSaleList());
        return invoiceModel;
    }

    @NonNull
    @Override
    public CollectionModel<InvoiceModel> toCollectionModel(@NonNull Iterable<? extends Invoice> invoiceList) {
        CollectionModel<InvoiceModel> collectionModel = super.toCollectionModel(invoiceList);
        collectionModel.add(linkTo(methodOn(InvoiceController.class).all()).withRel("all"));
        return collectionModel;
    }
}
