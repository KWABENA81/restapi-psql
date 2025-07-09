package com.asksef.assembler;

import com.asksef.controller.StaffController;
import com.asksef.entity.core.Payment;
import com.asksef.entity.core.Sale;
import com.asksef.entity.core.Staff;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.PaymentModel;
import com.asksef.entity.model.SaleModel;
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
        //
        staffModel.add(linkTo(methodOn(StaffController.class).one(entity.getStaffId())).withSelfRel());
        staffModel.add(linkTo(methodOn(StaffController.class).all()).withRel("all"));
        staffModel.add(linkTo(methodOn(StaffController.class).one(entity.getStaffId())).withSelfRel());
        //staffModel.add(linkTo(methodOn(StaffController.class).add(entity)).withRel("add"));
        staffModel.add(linkTo(methodOn(StaffController.class).delete(entity.getStaffId())).withRel("delete"));
//        staffModel.add(linkTo(methodOn(StaffController.class).findAddressOfStaff(entity.getStaffId())).withRel("Address"));
        staffModel.add(linkTo(methodOn(StaffController.class).findByUsername(entity.getUsername())).withRel("Username"));
        staffModel.add(linkTo(methodOn(StaffController.class).findStaffSales(entity.getStaffId())).withRel("Sales"));
        staffModel.add(linkTo(methodOn(StaffController.class).findStaffPayments(entity.getStaffId())).withRel("Payments"));
        staffModel.add(linkTo(methodOn(StaffController.class).findStaffStores(entity.getStaffId())).withRel("Stores"));

        staffModel.setStaffId(entity.getStaffId());
        staffModel.setFirstName(entity.getFirstName());
        staffModel.setLastName(entity.getLastName());
        staffModel.setUsername(entity.getUsername());
        staffModel.setLastUpdate(entity.getLastUpdate());
        //staffModel.setAddress(entity.getA);
        staffModel.setSaleModelList(toSaleCollectionModel(entity.getSaleList()));
        staffModel.setPaymentModelList(toPaymentCollectionModel(entity.getPaymentList()));
        staffModel.setStoreModelList(toStoreCollectionModel(entity.getStoreList()));
        return staffModel;
    }

    private List<PaymentModel> toPaymentCollectionModel(List<Payment> paymentList) {
        if (paymentList == null || paymentList.isEmpty()) {
            return Collections.emptyList();
        }
        return paymentList.stream()
                .map(pay -> PaymentModel.builder()
                        .paymentId(pay.getPaymentId())
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
                        .storeId(st.getStoreId())
                        .storeName(st.getStoreName())
                        .address(st.getAddress())
                        //.inventoryModelList(st.getInventoryList()
                        .staff(st.getStaff())
                        .lastUpdate(st.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(StaffController.class).one(st.getStoreId()))
                                .withRel("one"))).toList();
    }

    private List<SaleModel> toSaleCollectionModel(List<Sale> saleList) {
        if (saleList == null || saleList.isEmpty()) {
            return Collections.emptyList();
        }
        return saleList.stream()
                .map(sl -> SaleModel.builder()
                        .saleId(sl.getSaleId())
                        .saleDate(sl.getSaleDate())
                        .saleNr(sl.getSaleNr())
                        .staff(sl.getStaff())
                        .invoice(sl.getInvoice())
                        .lastUpdate(sl.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(StaffController.class)
                                .one(sl.getSaleId())).withRel("one"))).toList();
    }


    @NonNull
    @Override
    public CollectionModel<StaffModel> toCollectionModel(@NonNull Iterable<? extends Staff> entities) {
        CollectionModel<StaffModel> staffModels = super.toCollectionModel(entities);
        staffModels.add(linkTo(methodOn(StaffController.class).all()).withRel("all"));
        return staffModels;
    }
}
