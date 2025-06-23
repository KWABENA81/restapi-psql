package asksef.controller;

import asksef.assembler.CityModelAssembler;
import asksef.entity.City;
import asksef.entity.service_impl.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v0/city")
public class CityController {

    private static final Logger log = LoggerFactory.getLogger(CityController.class);
    private final CityService cityService;
    private final CityModelAssembler cityModelAssembler;

    public CityController(CityService cityService,
                          CityModelAssembler cityModelAssembler) {
        this.cityService = cityService;
        this.cityModelAssembler = cityModelAssembler;
        log.info("Inside CityController");
    }

    @GetMapping(value = "/all")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<City>> all() {
        List<EntityModel<City>> entityModelList = this.cityService.findAll().
                stream().map(cityModelAssembler::toModel).collect(Collectors.toList());
        log.info("Found {} cities", entityModelList.size());
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(CityController.class).all()).withSelfRel());
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<City> one(@PathVariable("id") Long id) {
        City city = this.cityService.findById(id);
        return this.cityModelAssembler.toModel(city);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Delete city with id: {}", id);
        this.cityService.delete(id);
        return new ResponseEntity<>("City entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    ///         ///////////////////////////////////////////////////////////

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
    @PostMapping(path = "/add", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<EntityModel<City>> add(@RequestBody City newCity) throws Exception {
        City savedCity = cityService.save(newCity);
        EntityModel<City> entityModel = this.cityModelAssembler.toModel(savedCity);

        log.info("Inside add to save city {}", savedCity);
        return //new ResponseEntity<>(savedCity, HttpStatus.CREATED);
        ResponseEntity.created(linkTo(methodOn(CityController.class)
                .add(savedCity)).toUri()).body(entityModel);
                //entityModel
                //.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody City newCity) {
        City updateCity = this.cityService.update(id, newCity);

        EntityModel<City> entityModel = this.cityModelAssembler.toModel(updateCity);
        log.info("Updating city with id: {}", id);
        return ResponseEntity.created(entityModel
                .getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
//        return ResponseEntity
//                .created(linkTo(methodOn(CityController.class).one(updateCity.getCityId()))
//                        .toUri()).body(entityModel);
    }
}
