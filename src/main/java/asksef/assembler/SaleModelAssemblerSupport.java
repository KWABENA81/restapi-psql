package asksef.assembler;

import asksef.controller.SaleController;
import asksef.entity.Sale;
import asksef.entity.entity_model.InvoiceModel;
import asksef.entity.entity_model.SaleModel;
import asksef.entity.entity_model.StaffModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class SaleModelAssemblerSupport extends RepresentationModelAssemblerSupport<Sale, SaleModel> {
    public SaleModelAssemblerSupport() {
        super(SaleController.class, SaleModel.class);
    }

    @NonNull
    @Override
    public SaleModel toModel(@NonNull Sale entity) {
        SaleModel saleModel = instantiateModel(entity);
        saleModel.add(linkTo(methodOn(SaleController.class).one(entity.getSaleId())).withSelfRel());

        saleModel.setSaleId(entity.getSaleId());
        saleModel.setSaleDate(entity.getSaleDate());
        saleModel.setSaleNr(entity.getSaleNr());
        saleModel.setLastUpdate(entity.getLastUpdate());
        saleModel.setStaff(entity.getStaff());
        saleModel.setInvoice(entity.getInvoice());

        return saleModel;
    }

    @NonNull
    @Override
    public CollectionModel<SaleModel> toCollectionModel(@NonNull Iterable<? extends Sale> entities) {
        CollectionModel<SaleModel> saleModes = super.toCollectionModel(entities);

        saleModes.add(linkTo(methodOn(SaleController.class).all()).withRel("all"));
        //saleModes.add(linkTo(methodOn(SaleController.class).one()))
        return saleModes;
    }
}
