package com.asksef.assembler;

import com.asksef.controller.AddressController;
import com.asksef.entity.core.Address;
import com.asksef.entity.core.Customer;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.AddressModel;
import com.asksef.entity.model.CustomerModel;
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
public class AddressModelAssemblerSupport extends RepresentationModelAssemblerSupport<Address, AddressModel> {

    public AddressModelAssemblerSupport() {
        super(AddressController.class, AddressModel.class);
    }

    @NonNull
    @Override
    public AddressModel toModel(@NonNull Address entity) {
        AddressModel addressModel = instantiateModel(entity);
        addressModel.setAddressId(entity.getAddressId());
        addressModel.setGpsCode(entity.getGpsCode());
        addressModel.setPhone(entity.getPhone());
        addressModel.setCity(entity.getCity());
        addressModel.setLastUpdate(entity.getLastUpdate());
        addressModel.setCustomerModelList(toCustomerCollectionModel(entity.getCustomerList()));
        addressModel.setStoreModelList(toStoreCollectionModel(entity.getStoreList()));

        addressModel.add(linkTo(methodOn(AddressController.class)
                .one(entity.getAddressId())).withSelfRel());
        addressModel.add(linkTo(methodOn(AddressController.class)
                .findCityOfAddress(entity.getAddressId())).withRel("cityOfAddress"));
        addressModel.add(linkTo(methodOn(AddressController.class)
                .all()).withRel("all"));
        addressModel.add(linkTo(methodOn(AddressController.class)
                .findByPhone(entity.getPhone())).withRel("phone"));
        addressModel.add(linkTo(methodOn(AddressController.class)
                .findAddressCustomers(entity.getAddressId())).withRel("Customers At Address"));
        addressModel.add(linkTo(methodOn(AddressController.class)
                .findAddressStores(entity.getAddressId())).withRel("Stores At Address"));
        return addressModel;
    }

    private List<StoreModel> toStoreCollectionModel(List<Store> stores) {
        if (stores == null || stores.isEmpty()) {
            return Collections.emptyList();
        }
        return stores.stream()
                .map(str -> StoreModel.builder()
                        .storeId(str.getStoreId())
                        .storeName(str.getStoreName())

                        .staff(str.getStaff())
                        .address(str.getAddress())
                        .lastUpdate(str.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(AddressController.class)
                                .one(str.getAddress().getAddressId()))
                                .withSelfRel())).toList();
    }

    private List<CustomerModel> toCustomerCollectionModel(List<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            return Collections.emptyList();
        }
        return customers.stream()
                .map(cust -> CustomerModel.builder()
                        .customerId(cust.getCustomerId())
                        .address(cust.getAddress())
                        .lastName(cust.getLastName())
                        .firstName(cust.getFirstName())
                        .lastUpdate(cust.getLastUpdate())
                        .creationDate(cust.getCreateDate())
                        .build()
                        .add(linkTo(methodOn(AddressController.class)
                                .one(cust.getCustomerId()))
                                .withSelfRel())).toList();
    }

    @NonNull
    @Override
    public CollectionModel<AddressModel> toCollectionModel(@NonNull Iterable<? extends Address> entities) {
        CollectionModel<AddressModel> addressModels = super.toCollectionModel(entities);
        addressModels.add(linkTo(methodOn(AddressController.class).all()).withRel("all"));
        return addressModels;
    }
}
