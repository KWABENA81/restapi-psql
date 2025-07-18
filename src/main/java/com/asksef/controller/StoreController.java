package com.asksef.controller;

import com.asksef.assembler.AddressModelAssemblerSupport;
import com.asksef.assembler.InventoryModelAssemblerSupport;
import com.asksef.assembler.StaffModelAssemblerSupport;
import com.asksef.assembler.StoreModelAssemblerSupport;
import com.asksef.entity.core.Address;
import com.asksef.entity.core.Inventory;
import com.asksef.entity.core.Staff;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.AddressModel;
import com.asksef.entity.model.InventoryModel;
import com.asksef.entity.model.StaffModel;
import com.asksef.entity.model.StoreModel;
import com.asksef.entity.repository.StoreRepository;
import com.asksef.entity.service_impl.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
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
    private final PagedResourcesAssembler<Store> pagedResourcesAssembler;
    private final StoreModelAssemblerSupport storeModelAssemblerSupport;

    public StoreController(StoreService storeService, StoreRepository storeRepository,
                           PagedResourcesAssembler<Store> pagedResourcesAssembler,
                           StoreModelAssemblerSupport storeModelAssemblerSupport) {
        this.storeService = storeService;
        this.storeRepository = storeRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
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
    public ResponseEntity<AddressModel> findStoreAddress(@PathVariable("id") Long id) {
        Address address = this.storeService.findStoreAddress(id);
        AddressModel model = new AddressModelAssemblerSupport().toModel(address);
        model.add(linkTo(methodOn(StoreController.class).one(id)).withSelfRel());
        model.add(linkTo(methodOn(StoreController.class).findStoreAddress(id)).withRel("Store Address"));
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/staff", produces = "application/hal+json")
    public ResponseEntity<StaffModel> findStoreStaff(@PathVariable("id") Long id) {
        Staff staff = this.storeService.findStoreStaff(id);
        //  build staff model
        StaffModel staffModel = new StaffModelAssemblerSupport().toModel(staff);
        staffModel.add(linkTo(methodOn(StoreController.class).one(id)).withRel("staff"));
        staffModel.add(linkTo(methodOn(StoreController.class).findStoreStaff(id)).withRel("staff"));
        return new ResponseEntity<>(staffModel, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/inventories", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<InventoryModel>> findStoreInventories(@PathVariable("id") Long id) {
        List<Inventory> inventoryList = storeService.findStoreInventories(id);
        CollectionModel<InventoryModel> inventoryModels = new InventoryModelAssemblerSupport().toCollectionModel(inventoryList);
        return new ResponseEntity<>(inventoryModels, HttpStatus.OK);
    }

    @GetMapping(value = "/paged")
    public ResponseEntity<PagedModel<StoreModel>> paged(Pageable pageable) {
        Page<Store> entityPage = storeService.findAll(pageable);

        PagedModel<StoreModel> pagedModel = pagedResourcesAssembler.toModel(entityPage, storeModelAssemblerSupport);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }
}
