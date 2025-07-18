package com.asksef.controller;

import com.asksef.assembler.InventoryModelAssemblerSupport;
import com.asksef.assembler.ItemModelAssemblerSupport;
import com.asksef.assembler.StoreModelAssemblerSupport;
import com.asksef.entity.core.Inventory;
import com.asksef.entity.core.Item;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.InventoryModel;
import com.asksef.entity.model.ItemModel;
import com.asksef.entity.model.StoreModel;
import com.asksef.entity.repository.InventoryRepository;
import com.asksef.entity.service_impl.InventoryService;
import lombok.NonNull;
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
@RequestMapping(value = "/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;
    private final PagedResourcesAssembler<Inventory> pagedResourcesAssembler;
    private final InventoryModelAssemblerSupport inventoryModelAssemblerSupport;

    public InventoryController(InventoryService service, InventoryRepository inventoryRepository,
                               PagedResourcesAssembler<Inventory> pagedResourcesAssembler,
                               InventoryModelAssemblerSupport modelAssemblerSupport) {
        this.inventoryService = service;
        this.inventoryRepository = inventoryRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.inventoryModelAssemblerSupport = modelAssemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<InventoryModel>> all() {
        List<Inventory> entityModelList = inventoryService.findAll().stream().toList();
        log.info("InventoryController allInventories");
        return new ResponseEntity<>(this.inventoryModelAssemblerSupport
                .toCollectionModel(entityModelList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InventoryModel> one(@PathVariable("id") Long id) {
        //Inventory inventory =
        return this.inventoryRepository.findById(id)
                .map(this.inventoryModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.inventoryService.delete(id);

        return new ResponseEntity<>("Inventory deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    public ResponseEntity<InventoryModel> add(@RequestBody InventoryModel inventoryModel) {
        Inventory savedInventory = this.inventoryService.save(inventoryModel);
        @NonNull InventoryModel entityModel = this.inventoryModelAssemblerSupport.toModel(savedInventory);
        log.info("InventoryController addInventory");
        //
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Inventory inventory) {
        Inventory updateInventory = inventoryService.update(id, inventory);

        @NonNull InventoryModel entityModel = this.inventoryModelAssemblerSupport.toModel(updateInventory);
        log.info("InventoryController updateInventory");
        //return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(InvoiceController.class).one(id))
                        .toUri()).body(entityModel);
    }

    @GetMapping(value = "{id}/item", produces = "application/hal+json")
    public ResponseEntity<ItemModel> findInventoryItem(@PathVariable("id") Long id) {
        Item item = this.inventoryService.findInventoryItem(id);
        //  build model
        ItemModel itemModel = new ItemModelAssemblerSupport().toModel(item);

        itemModel.add(linkTo(methodOn(InventoryController.class).findInventoryItem(id)).withSelfRel());
        return new ResponseEntity<>(itemModel, HttpStatus.OK);
    }

    @GetMapping(value = "{id}/store", produces = "application/hal+json")
    public ResponseEntity<StoreModel> findInventoryStore(@PathVariable("id") Long id) {
        Store store = this.inventoryService.findInventoryStore(id);
        //  build model
        StoreModel storemodel = new StoreModelAssemblerSupport().toModel(store);

        storemodel.add(linkTo(methodOn(InventoryController.class).findInventoryStore(id)).withRel("store of Inventory"));
        storemodel.add(linkTo(methodOn(InventoryController.class).one(id)).withRel("inventory"));
        storemodel.add(linkTo(methodOn(StoreController.class).one(id)).withRel("store"));
        return new ResponseEntity<>(storemodel, HttpStatus.OK);
    }

    @GetMapping(value = "/paged")
    public ResponseEntity<PagedModel<InventoryModel>> paged(Pageable pageable) {
        Page<Inventory> entityPage = inventoryService.findAll(pageable);

        PagedModel<InventoryModel> pagedModel = pagedResourcesAssembler.toModel(entityPage, inventoryModelAssemblerSupport);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }
}
