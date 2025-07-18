package com.asksef.assembler;

import com.asksef.controller.PaymentController;
import com.asksef.entity.core.Payment;
import com.asksef.entity.model.PaymentModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PaymentModelAssemblerSupport extends RepresentationModelAssemblerSupport<Payment, PaymentModel> {
    public PaymentModelAssemblerSupport() {
        super(PaymentController.class, PaymentModel.class);
    }

    @NonNull
    @Override
    public PaymentModel toModel(@NonNull Payment payment) {
        PaymentModel model = instantiateModel(payment);
        model.setPaymentNr(payment.getPaymentNr());
        model.setPaymentDate(payment.getPaymentDate());
        model.setAmount(payment.getAmount());
        model.setInvoice(payment.getInvoice());
        model.setStaff(payment.getStaff());
        //
        model.add(linkTo(methodOn(PaymentController.class).all()).withSelfRel());
        model.add(linkTo(methodOn(PaymentController.class).paymentByNr(payment.getPaymentNr())).withSelfRel());

        //model.add(linkTo(methodOn(PaymentController.class).all()).withRel("payments"));
        model.add(linkTo(methodOn(PaymentController.class).findPaymentInvoice(payment.getPaymentId())).withRel("Payment Invoice"));
        model.add(linkTo(methodOn(PaymentController.class).findStaffOnPayment(payment.getPaymentId())).withRel("Staff on Payment"));
        model.add(linkTo(methodOn(PaymentController.class).one(payment.getPaymentId())).withSelfRel());

        return model;
    }

    @NonNull
    @Override
    public CollectionModel<PaymentModel> toCollectionModel(@NonNull Iterable<? extends Payment> payments) {
        CollectionModel<PaymentModel> paymentModels = super.toCollectionModel(payments);
        paymentModels.add(linkTo(methodOn(PaymentController.class).all()).withSelfRel());
        return paymentModels;
    }
}
