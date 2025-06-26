package asksef.controller;

import asksef.assembler.CountryModelAssembler;
import asksef.entity.Country;
import asksef.entity.dto.CountryTransferObj;
import asksef.entity.service_impl.CountryService;
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
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/country")
public class CountryController {
    private static final Logger log = LoggerFactory.getLogger(CountryController.class);

    private final CountryModelAssembler countryModelAssembler;
    private final CountryService countryService;

    public CountryController(CountryModelAssembler assembler, CountryService countryService) {
        this.countryModelAssembler = assembler;
        this.countryService = countryService;
        log.info("CountryController created");
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Country>> all() {
        List<EntityModel<Country>> entityModelList = this.countryService.findAll().
                stream().map(this.countryModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(CountryController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Country> one(@PathVariable("id") Long id) {
        Country country = this.countryService.findById(id);
        return this.countryModelAssembler.toModel(country);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.countryService.delete(id);
        return new ResponseEntity<>("Country entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Country>> findLikeName(@RequestParam(value = "name") String name) {
        Collection<EntityModel<Country>> entityModelList = this.countryService.findLikeName(name)
                .stream().map(countryModelAssembler::toModel).collect(Collectors.toList());

        log.info("Countries: {}", entityModelList);
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(CountryController.class).findLikeName(name)).withSelfRel());
    }


    @PostMapping(path = "/add", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<Country>> add(@RequestBody @Valid CountryTransferObj countryDto) {
        Country newCountry = this.countryService.save(countryDto);
        EntityModel<Country> entityModel = this.countryModelAssembler.toModel(newCountry);
        return ResponseEntity
                .created(linkTo(methodOn(CountryController.class).add(countryDto)).toUri()).body(entityModel);
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