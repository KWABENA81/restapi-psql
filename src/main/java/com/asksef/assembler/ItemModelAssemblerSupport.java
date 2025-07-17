package com.asksef.assembler;

import com.asksef.controller.ItemController;
import com.asksef.entity.core.Inventory;
import com.asksef.entity.core.Item;
import com.asksef.entity.core.Order;
import com.asksef.entity.model.InventoryModel;
import com.asksef.entity.model.ItemModel;
import com.asksef.entity.model.OrderModel;
import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ItemModelAssemblerSupport extends RepresentationModelAssemblerSupport<Item, ItemModel> {
    public ItemModelAssemblerSupport() {
        super(ItemController.class, ItemModel.class);
    }

    @NonNull
    @Override
    public ItemModel toModel(@NonNull Item item) {
        ItemModel itemModel = instantiateModel(item);
        itemModel.setItemCode(item.getItemCode());
        itemModel.setItemName(item.getItemName());
        itemModel.setItemDesc(item.getItemDesc());
        itemModel.setItemCost(item.getItemCost());
        itemModel.setOrderModelList(toOrderCollectionModel(item.getOrderList()));
        itemModel.setInventoryModelList(toInventoryCollectionModel(item.getInventoryList()));

        itemModel.add(linkTo(methodOn(ItemController.class).all()).withRel("all"));
        itemModel.add(linkTo(methodOn(ItemController.class).itemByCode(item.getItemCode())).withRel("Item Code"));
        itemModel.add(linkTo(methodOn(ItemController.class).itemByNameLike(item.getItemName())).withRel("Item Name"));
        itemModel.add(linkTo(methodOn(ItemController.class).itemByDescLike(item.getItemDesc())).withRel("Item Desc"));
        itemModel.add(linkTo(methodOn(ItemController.class).findItemOrders(item.getItemId())).withRel("Sales"));
        itemModel.add(linkTo(methodOn(ItemController.class).findItemInventories(item.getItemId())).withRel("Inventories"));
        itemModel.add(linkTo(methodOn(ItemController.class).one(item.getItemId())).withSelfRel());

        return itemModel;
    }

    private List<OrderModel> toOrderCollectionModel(List<Order> orderList) {
        if (orderList == null || orderList.isEmpty()) {
            return Collections.emptyList();
        }
        return orderList.stream()
                .map(ord -> OrderModel.builder()
                        .orderNr(ord.getOrderNr())
                        .staff(ord.getStaff())
                        .item(ord.getItem())
                        .orderDate(ord.getOrderDate())
                        .lastUpdate(ord.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(ItemController.class).one(ord.getOrderId())).withRel("one"))).toList();
    }


    private List<InventoryModel> toInventoryCollectionModel(List<Inventory> inventoryList) {
        if (inventoryList == null || inventoryList.isEmpty()) {
            return Collections.emptyList();
        }
        return inventoryList.stream()
                .map(inv -> InventoryModel.builder()
                        .reorderQty(inv.getReorderQty())
                        .stockQty(inv.getStockQty())
                        .lastUpdate(inv.getLastUpdate())
                        .item(inv.getItem())
                        .store(inv.getStore())
                        .build()
                        .add(linkTo(methodOn(ItemController.class)
                                .one(inv.getInventoryId())).withRel("one"))).toList();
    }

    @NonNull
    @Override
    public CollectionModel<ItemModel> toCollectionModel(@NonNull Iterable<? extends Item> items) {
        CollectionModel<ItemModel> itemModels = super.toCollectionModel(items);
        itemModels.add(linkTo(methodOn(ItemController.class).all()).withRel("all"));

        return itemModels;
    }
}
