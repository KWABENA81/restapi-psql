package asksef.controller;

import asksef.assembler.StoreModelAssembler;
import asksef.entity.Store;
import asksef.entity.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/api/store")
public class StoreController {

    private static final Logger log = LoggerFactory.getLogger(StoreController.class);
    private final StoreService storeService;
    private final StoreModelAssembler storeModelAssembler;

    public StoreController(StoreService storeService, StoreModelAssembler storeModelAssembler) {
        this.storeService = storeService;
        this.storeModelAssembler = storeModelAssembler;
        log.info("Store Controller");
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Store>> all() {
        List<EntityModel<Store>> entityModelList = this.storeService.findAll().
                stream().map(storeModelAssembler::toModel).collect(Collectors.toList());
        log.info("All stores found");
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(StoreController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Store> one(@PathVariable Long id) {
        Store store = this.storeService.findById(id);
        log.info("Found store with id {}", id);
        return storeModelAssembler.toModel(store);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Store>> getStoreByName(@RequestParam(name = "storeName") String storeName) {
        List<EntityModel<Store>> entityModelList = this.storeService.findByStoreName(storeName)
                .stream().map(storeModelAssembler::toModel).toList();
        log.info("Found store with name {}", storeName);
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(StoreController.class).getStoreByName(storeName)).withSelfRel());
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(@RequestBody Store store) {
        Store savedStore = this.storeService.save(store);
        EntityModel<Store> storeEntityModel = storeModelAssembler.toModel(savedStore);
        log.info("Store added: {}", storeEntityModel);
//        return ResponseEntity.created(storeEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(storeEntityModel);
        return ResponseEntity
                .created(linkTo(methodOn(StoreController.class).one(savedStore.getStoreId()))
                        .toUri()).body(storeEntityModel);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Store newStore) {
        Store updateStore = this.storeService.update(id, newStore);
        EntityModel<Store> entityModel = storeModelAssembler.toModel(updateStore);
        log.info("Updating store with id: {}", id);
//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(StoreController.class).one(id))
                        .toUri()).body(entityModel);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.storeService.delete(id);
        log.info("Deleted store with id {}", id);
        return new ResponseEntity<>("Store entity deleted successfully.", HttpStatus.NO_CONTENT);
    }
}
