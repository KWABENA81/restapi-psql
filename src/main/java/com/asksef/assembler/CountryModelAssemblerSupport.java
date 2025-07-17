package com.asksef.assembler;

import com.asksef.controller.CountryController;
import com.asksef.entity.core.City;
import com.asksef.entity.core.Country;
import com.asksef.entity.model.CityModel;
import com.asksef.entity.model.CountryModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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
        countryModel.setCountry(entity.getCountry());
        countryModel.setLastUpdate(entity.getLastUpdate());
        countryModel.setCityModelList(toCityCollectionModel(entity.getCityList()));

              countryModel.add(linkTo(methodOn(CountryController.class)
                .all()).withRel("all"));
        countryModel.add(linkTo(methodOn(CountryController.class)
                .add(countryModel)).withRel("add"));
        countryModel.add(linkTo(methodOn(CountryController.class)
                .findByName(entity.getCountry())).withRel("findByName"));
        countryModel.add(linkTo(methodOn(CountryController.class)
                .findCountryCities(entity.getCountryId())).withRel("Cities in Country"));
        countryModel.add(linkTo(methodOn(CountryController.class)
                .one(entity.getCountryId())).withSelfRel());
        //
        return countryModel;
    }

    @NonNull
    @Override
    public CollectionModel<CountryModel> toCollectionModel(@NonNull Iterable<? extends Country> entities) {
        CollectionModel<CountryModel> countryModels = super.toCollectionModel(entities);
        countryModels.add(linkTo(methodOn(CountryController.class).all()).withRel("all"));
        //
        return countryModels;
    }

    public List<CityModel> toCityCollectionModel(List<City> cities) {
        if (cities == null || cities.isEmpty()) {
            return Collections.emptyList();
        }
        return cities.stream()
                .map(cty -> CityModel.builder()
                        .city(cty.getCity())
                        .country(cty.getCountry())
                        .lastUpdate(cty.getLastUpdate())
                        .build()
                        .add(linkTo(methodOn(CountryController.class)
                                .one(cty.getCountry().getCountryId()))
                                .withSelfRel())).toList();
    }

}
