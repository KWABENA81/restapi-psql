package asksef.controller;

import asksef.assembler.CityModelAssembler;
import asksef.assembler_support.CityModelAssemblerSupport;
import asksef.entity.City;
import asksef.entity.entity_model.CityModel;
import asksef.entity.repository.CityRepository;
import asksef.entity.service.CityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/city")
public class CityController {

    private static final Logger log = LoggerFactory.getLogger(CityController.class);
    private final CityService cityService;
    private final CityModelAssembler cityModelAssembler;
    private final CityRepository cityRepository;
    private final CityModelAssemblerSupport assemblerSupport;

    public CityController(CityService cityService,
                          CityModelAssembler cityModelAssembler, CityRepository cityRepository,
                          CityModelAssemblerSupport cityModelAssemblerSupport) {
        this.cityService = cityService;
        this.cityModelAssembler = cityModelAssembler;
        this.cityRepository = cityRepository;
        this.assemblerSupport = cityModelAssemblerSupport;
    }

    @GetMapping(value = "/all")
    public ResponseEntity<CollectionModel<CityModel>> all() {
        List<City> entityList = this.cityService.findAll().stream().toList();
        return new ResponseEntity<>(this.assemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CityModel> one(@PathVariable("id") Long id) {
       return this.cityRepository.findById(id)
               .map(assemblerSupport::toModel)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        this.cityService.delete(id);
        return new ResponseEntity<>("City entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

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
       public ResponseEntity<CityModel> add(@RequestBody CityModel cityModel)  {
        City savedCity = cityService.save(cityModel);
        CityModel entityModel = this.assemblerSupport.toModel(savedCity);
//        EntityModel<City> entityModel = this.cityModelAssembler.toModel(savedCity);
//        log.info("Inside add to save city {}", savedCity);
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
//                ResponseEntity.created(linkTo(methodOn(CityController.class)
//                        .add(savedCity)).toUri()).body(entityModel);        //entityModel
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
