package com.asksef.assembler;

import com.asksef.controller.AddressController;
import com.asksef.entity.core.Address;
import com.asksef.entity.model.AddressModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@Component
public class AddressModelAssemblerSupport extends RepresentationModelAssemblerSupport<Address, AddressModel> {

    public AddressModelAssemblerSupport() {
        super(AddressController.class, AddressModel.class);
    }

    @NonNull
    @Override
    public AddressModel toModel(@NonNull Address entity) {
        AddressModel addressModel = instantiateModel(entity);
        addressModel.add(
                linkTo(methodOn(AddressController.class).one(entity.getAddressId())).withSelfRel());
        addressModel.add(
                linkTo(methodOn(AddressController.class).findCityOfAddress(entity.getAddressId()))
                        .withRel("cityOfAddress"));
        addressModel.add(linkTo(methodOn(AddressController.class).all()).withRel("all"));
        addressModel.add(linkTo(methodOn(AddressController.class).findByPhone(entity.getPhone())).withRel("phone"));

        addressModel.setAddressId(entity.getAddressId());
        addressModel.setGpsCode(entity.getGpsCode());
        addressModel.setPhone(entity.getPhone());
        addressModel.setCity(entity.getCity());
        addressModel.setLastUpdate(entity.getLastUpdate());
       // addressModel.setCustomerList(entity.getCustomerList());
       // addressModel.setStoreList(entity.getStoreList());
        log.info("All addresses addressModel: {}", addressModel);
        return addressModel;
    }

    @NonNull
    @Override
    public CollectionModel<AddressModel> toCollectionModel(@NonNull Iterable<? extends Address> entities) {
        CollectionModel<AddressModel> addressModels = super.toCollectionModel(entities);
        addressModels.add(linkTo(methodOn(AddressController.class).all()).withRel("all"));
        log.info("All addresses Collections addressModels: {}", addressModels);
        return addressModels;
    }
}
