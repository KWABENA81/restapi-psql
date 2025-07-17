package com.asksef.assembler;

import com.asksef.controller.StaffController;
import com.asksef.entity.core.Order;
import com.asksef.entity.core.Payment;
import com.asksef.entity.core.Staff;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.OrderModel;
import com.asksef.entity.model.PaymentModel;
import com.asksef.entity.model.StaffModel;
import com.asksef.entity.model.StoreModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StaffModelAssemblerSupport extends RepresentationModelAssemblerSupport<Staff, StaffModel> {

    public StaffModelAssemblerSupport() {
        super(StaffController.class, StaffModel.class);
    }

    @NonNull
    @Override
    public StaffModel toModel(@NonNull Staff entity) {
        StaffModel staffModel = instantiateModel(entity);

        staffModel.setFirstName(entity.getFirstName());
        staffModel.setLastName(entity.getLastName());
        staffModel.setUsername(entity.getUsername());
        staffModel.setLastUpdate(entity.getLastUpdate());
        //staffModel.setAddress(entity.getA);
        staffModel.setOrderModelList(toOrderCollectionModel(entity.getOrderList()));
        staffModel.setPaymentModelList(toPaymentCollectionModel(entity.getPaymentList()));
        staffModel.setStoreModelList(toStoreCollectionModel(entity.getStoreList()));
        //
        staffModel.add(linkTo(methodOn(StaffController.class).all()).withRel("all"));
        staffModel.add(linkTo(methodOn(StaffController.class).one(entity.getStaffId())).withSelfRel());
        //staffModel.add(linkTo(methodOn(StaffController.class).add(entity)).withRel("add"));
        staffModel.add(linkTo(methodOn(StaffController.class).delete(entity.getStaffId())).withRel("delete"));
//        staffModel.add(linkTo(methodOn(StaffController.class).findAddressOfStaff(entity.getStaffId())).withRel("Address"));
        staffModel.add(linkTo(methodOn(StaffController.class).findByUsername(entity.getUsername())).withRel("Username"));
        staffModel.add(linkTo(methodOn(StaffController.class).findStaffOrders(entity.getStaffId())).withRel("Sales"));
        staffModel.add(linkTo(methodOn(StaffController.class).findStaffPayments(entity.getStaffId())).withRel("Payments"));
        staffModel.add(linkTo(methodOn(StaffController.class).findStaffStores(entity.getStaffId())).withRel("Stores"));
        staffModel.add(linkTo(methodOn(StaffController.class).one(entity.getStaffId())).withSelfRel());

        return staffModel;
    }

    private List<PaymentModel> toPaymentCollectionModel(List<Payment> paymentList) {
        if (paymentList == null || paymentList.isEmpty()) {
            return Collections.emptyList();
        }
        return paymentList.stream()
                .map(pay -> PaymentModel.builder()
                        .paymentNr(pay.getPaymentNr())
                        .paymentDate(pay.getPaymentDate())
                        .amount(pay.getAmount())
                        .staff(pay.getStaff())
                        .invoice(pay.getInvoice())
                        .lastUpdate(pay.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(StaffController.class).all()).withRel("all"))).toList();
    }

    private List<StoreModel> toStoreCollectionModel(List<Store> storeList) {
        if (storeList == null || storeList.isEmpty()) {
            return Collections.emptyList();
        }
        return storeList.stream()
                .map(st -> StoreModel.builder()
                        .storeName(st.getStoreName())
                        .address(st.getAddress())
                        //.inventoryModelList(st.getInventoryList()
                        .staff(st.getStaff())
                        .lastUpdate(st.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(StaffController.class).one(st.getStoreId()))
                                .withRel("one"))).toList();
    }

    private List<OrderModel> toOrderCollectionModel(List<Order> orderList) {
        if (orderList == null || orderList.isEmpty()) {
            return Collections.emptyList();
        }
        return orderList.stream()
                .map(od -> OrderModel.builder()
                        .orderDate(od.getOrderDate())
                        .orderNr(od.getOrderNr())
                        .staff(od.getStaff())
                        .lastUpdate(od.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(StaffController.class)
                                .one(od.getOrderId())).withRel("one"))).toList();
    }


    @NonNull
    @Override
    public CollectionModel<StaffModel> toCollectionModel(@NonNull Iterable<? extends Staff> entities) {
        CollectionModel<StaffModel> staffModels = super.toCollectionModel(entities);
        staffModels.add(linkTo(methodOn(StaffController.class).all()).withRel("all"));
        return staffModels;
    }
}
