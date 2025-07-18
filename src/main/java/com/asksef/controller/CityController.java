package com.asksef.controller;

import com.asksef.assembler.AddressModelAssemblerSupport;
import com.asksef.assembler.CityModelAssemblerSupport;
import com.asksef.assembler.CountryModelAssemblerSupport;
import com.asksef.entity.core.Address;
import com.asksef.entity.core.City;
import com.asksef.entity.core.Country;
import com.asksef.entity.model.AddressModel;
import com.asksef.entity.model.CityModel;
import com.asksef.entity.model.CountryModel;
import com.asksef.entity.repository.CityRepository;
import com.asksef.entity.service_impl.CityService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api/city")
public class CityController {

    private final CityService cityService;
    private final CityRepository cityRepository;
    private final PagedResourcesAssembler<City> pagedResourcesAssembler;
    private final CityModelAssemblerSupport cityModelAssemblerSupport;

    public CityController(CityService cityService, CityRepository cityRepository,
                          PagedResourcesAssembler<City> pagedResourcesAssembler,
                          CityModelAssemblerSupport cityModelAssemblerSupport) {
        this.cityService = cityService;
        this.cityRepository = cityRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.cityModelAssemblerSupport = cityModelAssemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<CityModel>> all() {
        List<City> entityList = this.cityService.findAll().stream().toList();
        return new ResponseEntity<>(this.cityModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<CityModel> one(@PathVariable("id") Long id) {
        return this.cityRepository.findById(id)
                .map(cityModelAssemblerSupport::toModel)
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
        @NonNull CityModel entityModel = this.cityModelAssemblerSupport.toModel(savedCity);
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody City newCity) {
        City updateCity = this.cityService.update(id, newCity);

        CityModel entityModel = this.cityModelAssemblerSupport.toModel(updateCity);
        log.info("Updating city with id: {}", id);
        return ResponseEntity.created(entityModel
                .getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @GetMapping(value = "{id}/country", produces = "application/hal+json")
    public ResponseEntity<CountryModel> findCityCountry(@PathVariable("id") Long id) {
        Country country = cityService.findCityCountry(id);
        //  build a model
        CountryModel countryModel = new CountryModelAssemblerSupport().toModel(country);
        //  add links
        countryModel.add(linkTo(methodOn(CityController.class).findCityCountry(id)).withSelfRel());
        countryModel.add(linkTo(methodOn(CityController.class).one(id)).withRel("city"));
        return new ResponseEntity<>(countryModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/addresses", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<AddressModel>> findCityAddresses(@PathVariable("id") Long id) {
        List<Address> addressList = this.cityService.findCityAddresses(id);
        CollectionModel<AddressModel> addressModels = new AddressModelAssemblerSupport().toCollectionModel(addressList);

        return new ResponseEntity<>(addressModels, HttpStatus.OK);
    }

    @GetMapping(value = "/paged")
    public ResponseEntity<PagedModel<CityModel>> paged(Pageable pageable) {
        Page<City> entityPage = cityService.findAll(pageable);

        PagedModel<CityModel> cityModels = pagedResourcesAssembler.toModel(entityPage, cityModelAssemblerSupport);
        return new ResponseEntity<>(cityModels, HttpStatus.OK);
    }
}
