package com.asksef.assembler;

import com.asksef.controller.StoreController;
import com.asksef.entity.core.Inventory;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.InventoryModel;
import com.asksef.entity.model.StoreModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
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
        model.add(linkTo(methodOn(StoreController.class).findStoreStaff(entity.getStoreId())).withRel("staff"));
        model.add(linkTo(methodOn(StoreController.class).findStoreAddress(entity.getStoreId())).withRel("Address"));
        model.add(linkTo(methodOn(StoreController.class).findStoreInventories(entity.getStoreId())).withRel("Inventories"));
        model.add(linkTo(methodOn(StoreController.class).one(entity.getStoreId())).withSelfRel());
        model.add(linkTo(methodOn(StoreController.class).getStoreByName(entity.getStoreName())).withRel("store"));


        model.setStoreId(entity.getStoreId());
        model.setStoreName(entity.getStoreName());
        model.setStaff(model.getStaff());
        model.setAddress(entity.getAddress());
        model.setLastUpdate(entity.getLastUpdate());
        model.setInventoryModelList(toInventoryCollectionModel(entity.getInventoryList()));
        return model;
    }

    private List<InventoryModel> toInventoryCollectionModel(List<Inventory> inventoryList) {
        if (inventoryList == null || inventoryList.isEmpty()) {
            return Collections.emptyList();
        }
        return inventoryList.stream()
                .map(inv -> InventoryModel.builder()
                        .inventoryId(inv.getInventoryId())
                        .item(inv.getItem())
                        .store(inv.getStore())
                        .stockQty(inv.getStockQty())
                        .reorderQty(inv.getReorderQty())
                        .lastUpdate(inv.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(StoreController.class)
                                .one(inv.getInventoryId()))
                                .withSelfRel())).toList();
    }

    @NonNull
    @Override
    public CollectionModel<StoreModel> toCollectionModel(@NonNull Iterable<? extends Store> entities) {
        CollectionModel<StoreModel> storeModels = super.toCollectionModel(entities);
        storeModels.add(linkTo(methodOn(StoreController.class).all()).withSelfRel());
        return storeModels;
    }
}
