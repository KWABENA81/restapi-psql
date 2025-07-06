package asksef.assembler;

import asksef.controller.CountryController;
import asksef.entity.core.City;
import asksef.entity.core.Country;
import asksef.entity.model.CityModel;
import asksef.entity.model.CountryModel;
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
public class CountryModelAssemblerSupport extends RepresentationModelAssemblerSupport<Country, CountryModel> {
    public CountryModelAssemblerSupport() {
        super(CountryController.class, CountryModel.class);
    }

    @NonNull
    @Override
    public CountryModel toModel(@NonNull Country entity) {
        CountryModel countryModel = instantiateModel(entity);
        //
        countryModel.add(
                linkTo(methodOn(CountryController.class).one(entity.getCountryId())).withSelfRel());
        countryModel.add(
                linkTo(methodOn(CountryController.class).all()).withRel("all"));
        countryModel.add(
                linkTo(methodOn(CountryController.class).add(countryModel)).withRel("add"));
        countryModel.add(
                linkTo(methodOn(CountryController.class).findByName(entity.getCountry())).withRel("findByName"));
        countryModel.add(
                linkTo(methodOn(CountryController.class).countryCities(entity.getCountryId())).withRel("countryCities"));
        //
        countryModel.setCountryId(entity.getCountryId());
        countryModel.setCountry(entity.getCountry());
        countryModel.setLastUpdate(entity.getLastUpdate());
        // countryModel.setCityModels(toCityCollectionModel(entity.getCityList()));
        return countryModel;
    }

    @NonNull
    @Override
    public CollectionModel<CountryModel> toCollectionModel(@NonNull Iterable<? extends Country> entities) {
        CollectionModel<CountryModel> countryModels = super.toCollectionModel(entities);
        countryModels.add(linkTo(methodOn(CountryController.class).all()).withRel("all"));
        //  , linkTo(methodOn(CountryController.class).findLikeName()
        return countryModels;
    }

//    private List<CityModel> toCityModel(List<City> cities) {
//        if (cities == null || cities.isEmpty()) {
//            return Collections.emptyList();
//        }
//        return cities.stream().map(cty -> CityModel.builder()
//                .cityId(cty.getCityId())
//                .city(cty.getCity())
//                .addressList(cty.getAddressList())
//                .country(cty.getCountry())
//                .build()
//                .add(linkTo(methodOn(CountryController.class).one(cty.getCountry().getCountryId()))
//                        .withSelfRel())).collect(Collectors.toList());
//    }
//
//    private CountryModel toCountryModel(Country country) {
//        CountryModel countryModel = CountryModel.builder()
//                .countryId(country.getCountryId())
//                .country(country.getCountry())
//                .lastUpdate(country.getLastUpdate())
//                //.cityModels(toCityModel(country.getCityList()))
//                // .cityModels(toCityCollectionModel(country.getCityList()))
//                .build();
//        countryModel.add(
//                linkTo(methodOn(CountryController.class).findByName(country.getCountry())).withSelfRel());
//        countryModel.add(
//                linkTo(methodOn(CountryController.class).all()).withRel("all"));
//        countryModel.add(
//                linkTo(methodOn(CountryController.class).one(country.getCountryId())).withSelfRel());
////        countryModel.add(
////                linkTo(methodOn(CountryController.class).countryCities(country.getCountryId())).withSelfRel());
//        return countryModel;
//    }

    public List<CityModel> toCityCollectionModel(List<City> cities) {
        if (cities == null || cities.isEmpty()) {
            return Collections.emptyList();
        }
        return cities.stream()
                .map(cty -> CityModel.builder()
                        .cityId(cty.getCityId())
                        .city(cty.getCity())
                        .country(cty.getCountry())
                        .lastUpdate(cty.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(CountryController.class)
                                .one(cty.getCountry().getCountryId()))
                                .withSelfRel())).collect(Collectors.toList());
    }

}
