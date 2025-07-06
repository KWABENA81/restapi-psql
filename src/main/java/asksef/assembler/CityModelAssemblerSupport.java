package asksef.assembler;

import asksef.controller.CityController;
import asksef.entity.core.City;
import asksef.entity.model.CityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

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
                .findCityAddresses(entity.getCityId())).withRel("addressesOfCity"));

        cityModel.add(linkTo(methodOn(CityController.class).all()).withRel("all"));
        cityModel.add(linkTo(methodOn(CityController.class).add(cityModel)).withSelfRel());

        cityModel.setCityId(entity.getCityId());
        cityModel.setCity(entity.getCity());
            cityModel.setCountry(entity.getCountry());
        cityModel.setLastUpdate(entity.getLastUpdate());
        //cityModel.setAddressModels(toAddressModel(entity.getAddressList()));
        return cityModel;
    }

    @NonNull
    @Override
    public CollectionModel<CityModel> toCollectionModel(@NonNull Iterable<? extends City> cities) {
        CollectionModel<CityModel> cityModels = super.toCollectionModel(cities);
        cityModels.add(linkTo(methodOn(CityController.class).all()).withRel("all"));
        return cityModels;
    }

    public City toEntity(CityModel cityModel) {
        return City.builder()
                .id(cityModel.getCityId())
                .city(cityModel.getCity())
                .country(cityModel.getCountry())
                .lastUpdate(cityModel.getLastUpdate())
                .build();
    }
//    private List<AddressModel> toAddressModel(List<Address> addressList) {
//        if (addressList.isEmpty()) {
//            return Collections.emptyList();
//        }
//        return addressList.stream()
//                .map(addr -> AddressModel.builder()
//                        .addressId(addr.getAddressId())
//                        .city(addr.getCity())
//                        .gpsCode(addr.getGpsCode())
//                        .phone(addr.getPhone())
//                        .lastUpdate(addr.getLastUpdate())
//                        .build()
//                        .add(linkTo(methodOn(AddressController.class)
//                                .one(addr.getAddressId()))
//                                .withSelfRel())).collect(Collectors.toList());
//    }


}
