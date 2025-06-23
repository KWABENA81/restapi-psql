package asksef.assembler;

import asksef.controller.StoreController;
import asksef.entity.Store;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NonNullApi
public class StoreModelAssembler implements RepresentationModelAssembler<Store, EntityModel<Store>> {
    @Override
    public EntityModel<Store> toModel(Store store) {
        return EntityModel.of(store,
                linkTo(methodOn(StoreController.class).one(store.getStoreId())).withSelfRel(),
                linkTo(methodOn(StoreController.class).getStoreByName(store.getStoreName())).withSelfRel(),
                linkTo(methodOn(StoreController.class).all()).withRel("all"));
    }
}