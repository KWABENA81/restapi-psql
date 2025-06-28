package asksef.assembler_support;

import asksef.controller.PaymentController;
import asksef.entity.Payment;
import asksef.entity.entity_model.PaymentModel;
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
        PaymentModel paymentModel = instantiateModel(payment);
        //
        paymentModel.setPaymentId(payment.getPaymentId());
        paymentModel.setPaymentNr(payment.getPaymentNr());
        paymentModel.setPaymentDate(payment.getPaymentDate());
        paymentModel.setAmount(payment.getAmount());
//        paymentModel.setInvoice(payment.getInvoice());
//        paymentModel.setStaff(payment.getStaff());
        return paymentModel;
    }

    @NonNull
    @Override
    public CollectionModel<PaymentModel> toCollectionModel(@NonNull Iterable<? extends Payment> payments) {
        CollectionModel<PaymentModel> paymentModels = super.toCollectionModel(payments);
        paymentModels.add(linkTo(methodOn(PaymentController.class).all()).withSelfRel());
        return paymentModels;
    }
}
