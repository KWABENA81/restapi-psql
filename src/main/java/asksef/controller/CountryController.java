package asksef.controller;

import asksef.assembler.CountryModelAssembler;
import asksef.assembler_support.CountryModelAssemblerSupport;
import asksef.entity.City;
import asksef.entity.Country;
import asksef.entity.entity_dto.CountryTransferObj;
import asksef.entity.entity_model.CityModel;
import asksef.entity.entity_model.CountryModel;
import asksef.entity.repository.CountryRepository;
import asksef.entity.service.CountryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/country")
public class CountryController {
    private static final Logger log = LoggerFactory.getLogger(CountryController.class);

    private final CountryModelAssembler countryModelAssembler;
    private final CountryService countryService;
    private final CountryRepository countryRepository;
    private final CountryModelAssemblerSupport assemblerSupport;

    public CountryController(CountryModelAssembler assembler,
                             CountryService countryService,
                             CountryRepository countryRepository,
                             CountryModelAssemblerSupport modelAssemblerSupport) {
        this.countryModelAssembler = assembler;
        this.countryService = countryService;
        this.countryRepository = countryRepository;
        this.assemblerSupport = modelAssemblerSupport;
    }

    @GetMapping("/all")
    public ResponseEntity<CollectionModel<CountryModel>> all() {
        List<Country> entityList = this.countryService.findAll().stream().toList();
        return new ResponseEntity<>(this.assemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/city", produces = "application/hal+json")
    public ResponseEntity<Collection<CityModel>> countryCities(@PathVariable("id") Long id) {
        List<City> cityList = countryService.findById(id).getCityList().stream().toList();
        Collection<CityModel> cityModels = this.assemblerSupport.toCityCollectionModel(cityList);
        return new ResponseEntity<>(cityModels, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<CountryModel> findByName(@RequestParam(value = "name") String name) {
        return this.countryService.findByName(name)
                .map(assemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryModel> one(@PathVariable("id") Long id) {
        return this.countryRepository.findById(id)
                .map(assemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        this.countryService.delete(id);
        return new ResponseEntity<>("Country entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @PostMapping(path = "/add", produces = "application/json")
    public ResponseEntity<CountryModel> add(@RequestBody @Valid CountryModel countryModel) {
        Country savedCountry = this.countryService.save(countryModel);
        CountryModel entityModel = this.assemblerSupport.toModel(savedCountry);
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Country newCountry) {
        CountryTransferObj countryTransferObj =
                CountryTransferObj.builder().country(newCountry.getCountry()).build();

        Country updateCountry = this.countryService.update(id, countryTransferObj);
        EntityModel<Country> entityModel = this.countryModelAssembler.toModel(updateCountry);
        log.info("Updated country: {}", entityModel);
        //  Link link = entityLinks.linkToItemResource(updateCountry);
//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity.created(linkTo(methodOn(CountryController.class).one(id))
                .toUri()).body(entityModel);
    }

}
