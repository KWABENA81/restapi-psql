package asksef.assembler;

import asksef.controller.PaymentController;
import asksef.entity.Payment;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NonNullApi
public class PaymentModelAssembler implements RepresentationModelAssembler<Payment, EntityModel<Payment>> {
    @Override
    public EntityModel<Payment> toModel(Payment payment) {
        try {
            return EntityModel.of(payment,
                    linkTo(methodOn(PaymentController.class).one(payment.getPaymentId())).withSelfRel(),
                   // linkTo(methodOn(PaymentController.class).add(payment)).withSelfRel(),
                    linkTo(methodOn(PaymentController.class).paymentByNr(payment.getPaymentNr())).withSelfRel(),
                  //  linkTo(methodOn(PaymentController.class).delete(payment.getPaymentId())).withSelfRel(),
                   // linkTo(methodOn(PaymentController.class).update(payment.getPaymentId(), payment)).withSelfRel(),
                    linkTo(methodOn(PaymentController.class).all()).withRel("all"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
