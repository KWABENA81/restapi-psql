package com.asksef.assembler;

import com.asksef.controller.StaffController;
import com.asksef.entity.core.Staff;
import com.asksef.entity.model.StaffModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

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

        staffModel.setStaffId(entity.getStaffId());
        staffModel.setFirstName(entity.getFirstName());
        staffModel.setLastName(entity.getLastName());
        staffModel.setUsername(entity.getUsername());
        staffModel.setLastUpdate(entity.getLastUpdate());
        //staffModel.setAddress(entity.getA);
//        staffModel.setPaymentModels(toPaymentCollectionModel(entity.getPaymentList()));
//        staffModel.setSaleModels(toSaleCollectionModel(entity.getSaleList()));
//        staffModel.setStoreModels(toStoreCollectionModel(entity.getStoreList()));
        return staffModel;
    }

//    private List<SaleModel> toSaleCollectionModel(List<Sale> saleList) {
//        saleModels =super.toCollectionModel(saleList);
//    }
//
//    private List<StoreModel> toStoreCollectionModel(List<Store> storeList) {
//    }
//
//    private List<PaymentModel> toPaymentCollectionModel(List<Payment> paymentList) {
//    }

    @NonNull
    @Override
    public CollectionModel<StaffModel> toCollectionModel(@NonNull Iterable<? extends Staff> entities) {
        CollectionModel<StaffModel> staffModels = super.toCollectionModel(entities);
        staffModels.add(linkTo(methodOn(StaffController.class).all()).withRel("all"));
        return staffModels;
    }
}
