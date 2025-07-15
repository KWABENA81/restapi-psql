package com.asksef.assembler;

import com.asksef.controller.InventoryController;
import com.asksef.entity.core.Inventory;
import com.asksef.entity.model.InventoryModel;
import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class InventoryModelAssemblerSupport extends RepresentationModelAssemblerSupport<Inventory, InventoryModel> {
    public InventoryModelAssemblerSupport() {
        super(InventoryController.class, InventoryModel.class);
    }

    @NonNull
    @Override
    public InventoryModel toModel(@NonNull Inventory entity) {
        InventoryModel inventoryModel = instantiateModel(entity);
        //
        inventoryModel.add(linkTo(methodOn(InventoryController.class).one(entity.getInventoryId())).withRel("inventory"));
        inventoryModel.add(linkTo(methodOn(InventoryController.class).all()).withRel("all"));
        inventoryModel.add(linkTo(methodOn(InventoryController.class).findInventoryItem(entity.getInventoryId())).withRel("Inventory Item"));
        inventoryModel.add(linkTo(methodOn(InventoryController.class).findInventoryStore(entity.getInventoryId())).withRel("Store with Inventory"));

        inventoryModel.setInventoryId(entity.getInventoryId());
        inventoryModel.setItem(entity.getItem());
        inventoryModel.setStore(entity.getStore());
        inventoryModel.setStockQty(entity.getStockQty());
        inventoryModel.setReorderQty(entity.getReorderQty());
        inventoryModel.setLastUpdate(entity.getLastUpdate());
        return inventoryModel;
    }

    @NonNull
    @Override
    public CollectionModel<InventoryModel> toCollectionModel(@NonNull Iterable<? extends Inventory> entities) {
        CollectionModel<InventoryModel> models = super.toCollectionModel(entities);
        models.add(linkTo(methodOn(InventoryController.class).all()).withSelfRel());
        return models;
    }
}
