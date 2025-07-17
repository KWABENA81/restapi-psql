package com.asksef.assembler;

import com.asksef.controller.OrderController;
import com.asksef.entity.core.Order;
import com.asksef.entity.model.OrderModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderModelAssemblerSupport extends RepresentationModelAssemblerSupport<Order, OrderModel> {
    public OrderModelAssemblerSupport() {
        super(OrderController.class, OrderModel.class);
    }

    @NonNull
    @Override
    public OrderModel toModel(@NonNull Order entity) {
        OrderModel orderModel = instantiateModel(entity);

        orderModel.setOrderDate(entity.getOrderDate());
        orderModel.setOrderNr(entity.getOrderNr());
        orderModel.setLastUpdate(entity.getLastUpdate());
        orderModel.setStaff(entity.getStaff());
        orderModel.setItem(entity.getItem());

        orderModel.add(linkTo(methodOn(OrderController.class).one(entity.getOrderId())).withSelfRel());
        orderModel.add(linkTo(methodOn(OrderController.class).all()).withRel("orders"));
        orderModel.add(linkTo(methodOn(OrderController.class).findOrderStaff(entity.getOrderId())).withRel("orders staff"));
        orderModel.add(linkTo(methodOn(OrderController.class).findOrderItem(entity.getOrderId())).withRel("order item"));
        // orderModel.add(linkTo(methodOn(OrderController.class).findOrderItem(entity.getOrderId())).withRel("order item"));
        return orderModel;
    }

    @NonNull
    @Override
    public CollectionModel<OrderModel> toCollectionModel(@NonNull Iterable<? extends Order> entities) {
        CollectionModel<OrderModel> saleModes = super.toCollectionModel(entities);

        saleModes.add(linkTo(methodOn(OrderController.class).all()).withRel("all"));
        //saleModes.add(linkTo(methodOn(OrderController.class).one()))
        return saleModes;
    }
}
