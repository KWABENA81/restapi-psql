package asksef.assembler;

import asksef.controller.InvoiceController;
import asksef.entity.Invoice;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NonNullApi
public class InvoiceModelAssembler implements RepresentationModelAssembler<Invoice, EntityModel<Invoice>> {
    @Override
    public EntityModel<Invoice> toModel(Invoice invoice) {
        try {
            return EntityModel.of(invoice,
                    linkTo(methodOn(InvoiceController.class).one(invoice.getInvoiceId())).withSelfRel(),
                    linkTo(methodOn(InvoiceController.class).add(invoice)).withSelfRel(),
                    linkTo(methodOn(InvoiceController.class).delete(invoice.getInvoiceId())).withSelfRel(),
                  //  linkTo(methodOn(InvoiceController.class).update(invoice.getInvoiceId(), invoice)).withSelfRel(),
                    linkTo(methodOn(InvoiceController.class).all()).withRel("all"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}