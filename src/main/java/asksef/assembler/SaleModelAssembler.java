package asksef.assembler;

import asksef.controller.SaleController;
import asksef.entity.Sale;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NonNullApi
public class SaleModelAssembler implements RepresentationModelAssembler<Sale, EntityModel<Sale>> {
    @Override
    public EntityModel<Sale> toModel(Sale sale) {
        return EntityModel.of(sale,
                linkTo(methodOn(SaleController.class).one(sale.getSaleId())).withSelfRel(),
                linkTo(methodOn(SaleController.class).findBySaleNr(sale.getSaleNr())).withSelfRel(),
                linkTo(methodOn(SaleController.class).add(sale)).withSelfRel(),
                linkTo(methodOn(SaleController.class).delete(sale.getSaleId())).withSelfRel(),
                linkTo(methodOn(SaleController.class).update(sale.getSaleId(), sale)).withSelfRel(),
                linkTo(methodOn(SaleController.class).all()).withRel("all"));
    }
}
