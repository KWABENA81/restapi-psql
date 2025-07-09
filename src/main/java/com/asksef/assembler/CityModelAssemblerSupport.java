package com.asksef.assembler;

import com.asksef.controller.CityController;
import com.asksef.entity.core.Address;
import com.asksef.entity.core.City;
import com.asksef.entity.model.AddressModel;
import com.asksef.entity.model.CityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CityModelAssemblerSupport extends RepresentationModelAssemblerSupport<City, CityModel> {

    public CityModelAssemblerSupport() {
        super(CityController.class, CityModel.class);
    }

    @NonNull
    @Override
    public CityModel toModel(@NonNull City entity) {
        CityModel cityModel = instantiateModel(entity);
        cityModel.add(
                linkTo(methodOn(CityController.class).one(entity.getCityId())).withSelfRel());
        cityModel.add(
                linkTo(methodOn(CityController.class).findCountryOfCity(entity.getCityId())).withRel("countryOfCity"));
        cityModel.add(linkTo(methodOn(CityController.class)
                .findAddressesInCity(entity.getCityId())).withRel("addresses In City"));

        cityModel.add(linkTo(methodOn(CityController.class).all()).withRel("all"));
        cityModel.add(linkTo(methodOn(CityController.class).add(cityModel)).withSelfRel());

        cityModel.setCityId(entity.getCityId());
        cityModel.setCity(entity.getCity());
        cityModel.setCountry(entity.getCountry());
        cityModel.setLastUpdate(entity.getLastUpdate());
        cityModel.setAddressModelList(toAddressCollectionModel(entity.getAddressList()));
        return cityModel;
    }

    private List<AddressModel> toAddressCollectionModel(List<Address> addressList) {
        if (addressList == null || addressList.isEmpty()) {
            return Collections.emptyList();
        }
        return addressList.stream()
                .map(addr -> AddressModel.builder()
                        .addressId(addr.getAddressId())
                        .city(addr.getCity())
                        .phone(addr.getPhone())
                        .gpsCode(addr.getGpsCode())
                        .build()
                        .add(linkTo(methodOn(CityController.class)
                                .one(addr.getAddressId())).withSelfRel())).collect(Collectors.toList());
    }

    @NonNull
    @Override
    public CollectionModel<CityModel> toCollectionModel(@NonNull Iterable<? extends City> cities) {
        CollectionModel<CityModel> cityModels = super.toCollectionModel(cities);
        cityModels.add(linkTo(methodOn(CityController.class).all()).withRel("all"));
        return cityModels;
    }

}
