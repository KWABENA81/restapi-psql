package asksef.controller;

import asksef.assembler.AddressModelAssemblerSupport;
import asksef.assembler.StaffModelAssemblerSupport;
import asksef.assembler.StoreModelAssemblerSupport;
import asksef.entity.core.Address;
import asksef.entity.core.Staff;
import asksef.entity.core.Store;
import asksef.entity.model.AddressModel;
import asksef.entity.model.StaffModel;
import asksef.entity.model.StoreModel;
import asksef.entity.repository.StoreRepository;
import asksef.entity.service_impl.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api/store")
public class StoreController {

    private final StoreService storeService;
    private final StoreRepository storeRepository;
    private final StoreModelAssemblerSupport storeModelAssemblerSupport;

    public StoreController(StoreService storeService, StoreRepository storeRepository,
                           StoreModelAssemblerSupport storeModelAssemblerSupport) {
        this.storeService = storeService;
        this.storeRepository = storeRepository;
        this.storeModelAssemblerSupport = storeModelAssemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<StoreModel>> all() {
        List<Store> entityList = this.storeService.findAll().stream().toList();
        return new ResponseEntity<>(this.storeModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<StoreModel> one(@PathVariable("id") Long id) {
        return this.storeRepository.findById(id)
                .map(storeModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<CollectionModel<StoreModel>> getStoreByName(@RequestParam(name = "storeName") String storeName) {
        List<Store> entityModelList = this.storeService.findByStoreName(storeName).stream().toList();
        log.info("Found store with name {}", storeName);
        return new ResponseEntity<>(storeModelAssemblerSupport.toCollectionModel(entityModelList), HttpStatus.FOUND);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    public ResponseEntity<?> add(@RequestBody StoreModel storeModel) {
        Store savedStore = this.storeService.save(storeModel);
        StoreModel storeModelSup = storeModelAssemblerSupport.toModel(savedStore);
        log.info("Store added: {}", storeModel);
        return new ResponseEntity<>(storeModelSup, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Store newStore) {
        Store updateStore = this.storeService.update(id, newStore);
        StoreModel model = storeModelAssemblerSupport.toModel(updateStore);

        return ResponseEntity
                .created(linkTo(methodOn(StoreController.class).one(id))
                        .toUri()).body(model);
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.storeService.delete(id);
        log.info("Deleted store with id {}", id);
        return new ResponseEntity<>("Store entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}/address", produces = "application/hal+json")
    public ResponseEntity<AddressModel> findAddressOfStore(@PathVariable("id") Long id) {
        Address address = this.storeService.findAddressOfStore(id);
        AddressModel model = new AddressModelAssemblerSupport().toModel(address);
        model.add(linkTo(methodOn(StoreController.class).one(id)).withSelfRel());
        model.add(linkTo(methodOn(StoreController.class).findAddressOfStore(id)).withRel("Store Address"));
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/staff", produces = "application/hal+json")
    public ResponseEntity<StaffModel> findStaffOfStore(@PathVariable("id") Long id) {
        Staff staff = this.storeService.findStaffOfStore(id);
        //  build staff model
        StaffModel staffModel = new StaffModelAssemblerSupport().toModel(staff);
        staffModel.add(linkTo(methodOn(StoreController.class).one(id)).withRel("staff"));
        staffModel.add(linkTo(methodOn(StoreController.class).findStaffOfStore(id)).withRel("staff"));
        return new ResponseEntity<>(staffModel, HttpStatus.OK);
    }
}
