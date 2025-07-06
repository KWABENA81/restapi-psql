package com.asksef.controller;

import com.asksef.assembler.CountryModelAssemblerSupport;
import com.asksef.entity.core.City;
import com.asksef.entity.core.Country;
import com.asksef.entity.model.CityModel;
import com.asksef.entity.model.CountryModel;
import com.asksef.entity.repository.CountryRepository;
import com.asksef.entity.service_impl.CountryService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api/country")
public class CountryController {
    private final CountryService countryService;
    private final CountryRepository countryRepository;
    private final CountryModelAssemblerSupport countryModelAssemblerSupport;

    public CountryController(CountryService service, CountryRepository repo, CountryModelAssemblerSupport cmas) {
        this.countryService = service;
        this.countryRepository = repo;
        this.countryModelAssemblerSupport = cmas;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<CountryModel>> all() {
        List<Country> entityList = this.countryService.findAll().stream().toList();
        return new ResponseEntity<>(this.countryModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/city", produces = "application/hal+json")
    public ResponseEntity<Collection<CityModel>> countryCities(@PathVariable("id") Long id) {
        List<City> cityList = countryService.findById(id).getCityList().stream().toList();
        Collection<CityModel> cityModels = this.countryModelAssemblerSupport.toCityCollectionModel(cityList);
        return new ResponseEntity<>(cityModels, HttpStatus.OK);
    }

    @GetMapping(produces = "application/hal+json")
    public ResponseEntity<CountryModel> findByName(@RequestParam(value = "name") String name) {
        return this.countryService.findByName(name)
                .map(countryModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<CountryModel> one(@PathVariable("id") Long id) {
        return this.countryRepository.findById(id)
                .map(countryModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        this.countryService.delete(id);
        return new ResponseEntity<>("Country entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    public ResponseEntity<CountryModel> add(@RequestBody @Valid CountryModel countryModel) {
        Country savedCountry = this.countryService.save(countryModel);
        CountryModel entityModel = this.countryModelAssemblerSupport.toModel(savedCountry);
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Country newCountry) {
        CountryModel countryModel = countryModelAssemblerSupport.toModel(newCountry);
//                CountryModel.builder()
//                .countryId(newCountry.getCountryId())
//                .country(newCountry.getCountry())
//                .lastUpdate(newCountry.getLastUpdate())
//                .build();

        Country updateCountry = this.countryService.update(id, countryModel);
        CountryModel updatedCountry = this.countryModelAssemblerSupport.toModel(updateCountry);
        //log.info("Updated country: {}", entityModel);
        //  Link link = entityLinks.linkToItemResource(updateCountry);
//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity.created(linkTo(methodOn(CountryController.class).one(id))
                .toUri()).body(updatedCountry);
    }

}
