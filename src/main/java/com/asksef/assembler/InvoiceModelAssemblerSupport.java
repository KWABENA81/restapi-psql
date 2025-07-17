package com.asksef.assembler;

import com.asksef.controller.InvoiceController;
import com.asksef.entity.core.Invoice;
import com.asksef.entity.core.Order;
import com.asksef.entity.core.Payment;
import com.asksef.entity.model.InvoiceModel;
import com.asksef.entity.model.OrderModel;
import com.asksef.entity.model.PaymentModel;
import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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
        //  invoiceModel.add(linkTo(methodOn(InvoiceController.class).add(entity)).withRel("add"));
        invoiceModel.add(linkTo(methodOn(InvoiceController.class)
                .findCustomerOnInvoice(entity.getInvoiceId())).withRel("Customer On Invoice"));
        invoiceModel.add(linkTo(methodOn(InvoiceController.class)
                .findInvoicePayments(entity.getInvoiceId())).withRel("Invoice Payments"));
        invoiceModel.add(linkTo(methodOn(InvoiceController.class)
                .findInvoiceOrder(entity.getInvoiceId())).withRel("Invoice Order"));
        invoiceModel.add(linkTo(methodOn(InvoiceController.class).one(entity.getInvoiceId())).withSelfRel());

        invoiceModel.setInvoiceNr(entity.getInvoiceNr());
        invoiceModel.setCustomer(entity.getCustomer());
        invoiceModel.setLastUpdate(entity.getLastUpdate());
        invoiceModel.setOrder(entity.getOrder());
        invoiceModel.setPaymentModelList(toPaymentCollectionModel(entity.getPaymentList()));
        return invoiceModel;
    }

    private List<PaymentModel> toPaymentCollectionModel(List<Payment> paymentList) {
        if (paymentList == null || paymentList.isEmpty()) {
            return Collections.emptyList();
        }
        return paymentList.stream()
                .map(pay -> PaymentModel.builder()
                        .paymentNr(pay.getPaymentNr())
                        .amount(pay.getAmount())
                        .invoice(pay.getInvoice())
                        .staff(pay.getStaff())
                        .lastUpdate(pay.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(InvoiceController.class)
                                .one(pay.getPaymentId())).withRel("one"))).toList();
    }

    private List<OrderModel> toSalesCollectionModel(List<Order> orderList) {
        if (orderList == null || orderList.isEmpty()) {
            return Collections.emptyList();
        }
        return orderList.stream()
                .map(od -> OrderModel.builder()
                        .orderDate(od.getOrderDate())
                        .orderNr(od.getOrderNr())
                        .staff(od.getStaff())
                        .lastUpdate(od.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(InvoiceController.class)
                                .one(od.getOrderId())).withRel("one"))).toList();
    }

    @NonNull
    @Override
    public CollectionModel<InvoiceModel> toCollectionModel(@NonNull Iterable<? extends Invoice> invoiceList) {
        CollectionModel<InvoiceModel> collectionModel = super.toCollectionModel(invoiceList);
        collectionModel.add(linkTo(methodOn(InvoiceController.class).all()).withRel("all"));
        return collectionModel;
    }
}
