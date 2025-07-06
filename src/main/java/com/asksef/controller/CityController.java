package com.asksef.controller;

import com.asksef.assembler.AddressModelAssemblerSupport;
import com.asksef.assembler.CityModelAssemblerSupport;
import com.asksef.assembler.CountryModelAssemblerSupport;
import com.asksef.entity.core.City;
import com.asksef.entity.core.Country;
import com.asksef.entity.model.AddressModel;
import com.asksef.entity.model.CityModel;
import com.asksef.entity.model.CountryModel;
import com.asksef.entity.repository.CityRepository;
import com.asksef.entity.service_impl.CityService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api/city")
public class CityController {

    private final CityService cityService;
    private final CityRepository cityRepository;
    private final CityModelAssemblerSupport assemblerSupport;

    public CityController(CityService cityService, CityRepository cityRepository,
                          CityModelAssemblerSupport cityModelAssemblerSupport) {
        this.cityService = cityService;
        this.cityRepository = cityRepository;
        this.assemblerSupport = cityModelAssemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<CityModel>> all() {
        List<City> entityList = this.cityService.findAll().stream().toList();
        return new ResponseEntity<>(this.assemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/address", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<AddressModel>> findCityAddresses(@PathVariable("id") Long id) {
        final AddressModelAssemblerSupport addressModelAssemblerSupport = new AddressModelAssemblerSupport();
        Collection<AddressModel> cityAddresses = this.cityService.findAddressesOfCity(id)
                .stream().map(addressModelAssemblerSupport::toModel)
                .collect(Collectors.toList());
        CollectionModel<AddressModel> addressModelCollection = CollectionModel.of(cityAddresses, linkTo((methodOn(CityController.class)
                .findCityAddresses(id))).withSelfRel());

        log.info(" 65 Address modelss {}", addressModelCollection);
        return ResponseEntity.ok(addressModelCollection);
        // } else
        // return ResponseEntity.notFound().build();//<>( HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<CityModel> one(@PathVariable("id") Long id) {
        return this.cityRepository.findById(id)
                .map(assemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        this.cityService.delete(id);
        return new ResponseEntity<>("City entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/add", produces = "application/hal+json")
    public ResponseEntity<CityModel> add(@RequestBody CityModel cityModel) {
        City savedCity = cityService.save(cityModel);
        @NonNull CityModel entityModel = this.assemblerSupport.toModel(savedCity);
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody City newCity) {
        City updateCity = this.cityService.update(id, newCity);

        CityModel entityModel = this.assemblerSupport.toModel(updateCity);
        log.info("Updating city with id: {}", id);
        return ResponseEntity.created(entityModel
                .getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
//        return ResponseEntity
//                .created(linkTo(methodOn(CityController.class).one(updateCity.getCityId()))
//                        .toUri()).body(entityModel);
    }

    @GetMapping(value = "{id}/country", produces = "application/hal+json")
    public ResponseEntity<CountryModel> findCountryOfCity(@PathVariable("id") Long id) {
        Country country = cityService.findCountryOfCity(id);

        //  build a model
        CountryModel countryModel = new CountryModelAssemblerSupport().toModel(country);
        //  add links
        countryModel.add(linkTo(methodOn(CityController.class).findCountryOfCity(id)).withSelfRel());
        countryModel.add(linkTo(methodOn(CityController.class).one(id)).withRel("city"));
        return new ResponseEntity<>(countryModel, HttpStatus.OK);
    }
}


//    @GetMapping(value = "/{id}/address", produces = "application/hal+json")
//    public ResponseEntity<CollectionModel<AddressModel>> findCityAddresses(@PathVariable("id") Long id) {
//           List<Address> cityAddresses = this.cityService.findCityAddresses(id);
//
//            final AddressModelAssemblerSupport addressModelAssemblerSupport = new AddressModelAssemblerSupport();
//            CollectionModel<AddressModel> addressModelCollection = addressModelAssemblerSupport.toCollectionModel(cityAddresses);
//
//            addressModelCollection.forEach(
//                    amc -> amc.add(linkTo(methodOn(CityController.class)
//                            .findCityAddresses(id)).withSelfRel()));
//            log.info(" 65 Address modelss {}", addressModelCollection);
//            return new ResponseEntity<>(addressModelCollection, HttpStatus.OK);
//       // } else
//           // return ResponseEntity.notFound().build();//<>( HttpStatus.OK);
//    }


//    @PostMapping(path = "/add",
//            produces = "application/ld+json"

/// /            produces = "application/json;charset=utf-8"
//    )
//    //@Produces({MediaType.APPLICATION_JSON_VALUE,"application/json;charset=utf-8"})
//    //@Produces("application/json;charset=utf-8")application/ld+json
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<EntityModel<City>> add(@RequestBody City city) throws Exception {
//        City savedCity = cityService.save(city);
//
//        log.info("Inside add to save city {}",savedCity);
//        //City newCity = this.cityService.findById(savedCityDTO.getCityId());
//                EntityModel<City> entityModel = this.cityModelAssembler.toModel(savedCity);
//        log.info("Saved new city: {}", entityModel);
//        return ResponseEntity
//                .created(linkTo(methodOn(CityController.class).add(savedCity)).toUri()).body(entityModel);
//    }