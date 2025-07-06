package com.asksef.assembler;

import com.asksef.controller.StoreController;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.StoreModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StoreModelAssemblerSupport extends RepresentationModelAssemblerSupport<Store, StoreModel> {
    public StoreModelAssemblerSupport() {
        super(StoreController.class, StoreModel.class);
    }

    @NonNull
    @Override
    public StoreModel toModel(@NonNull Store entity) {
        StoreModel model = instantiateModel(entity);

        model.add(linkTo(methodOn(StoreController.class).all()).withSelfRel());
        model.add(linkTo(methodOn(StoreController.class).all()).withRel("store"));
        model.add(linkTo(methodOn(StoreController.class).findStaffOfStore(entity.getStoreId())).withRel("staff"));
        model.add(linkTo(methodOn(StoreController.class).findAddressOfStore(entity.getStoreId())).withRel("Address"));
        model.add(linkTo(methodOn(StoreController.class).one(entity.getStoreId())).withSelfRel());
        model.add(linkTo(methodOn(StoreController.class).getStoreByName(entity.getStoreName())).withRel("store"));

        model.setStoreId(entity.getStoreId());
        model.setStoreName(entity.getStoreName());
        model.setStaff(model.getStaff());
        return model;
    }

    @NonNull
    @Override
    public CollectionModel<StoreModel> toCollectionModel(@NonNull Iterable<? extends Store> entities) {
        CollectionModel<StoreModel> storeModels = super.toCollectionModel(entities);
        storeModels.add(linkTo(methodOn(StoreController.class).all()).withSelfRel());
        return storeModels;
    }
}
