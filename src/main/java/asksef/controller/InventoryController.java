package asksef.controller;

import asksef.assembler.InventoryModelAssemblerSupport;
import asksef.assembler.ItemModelAssemblerSupport;
import asksef.assembler_support.StoreModelAssemblerSupport;
import asksef.entity.Inventory;
import asksef.entity.Item;
import asksef.entity.Store;
import asksef.entity.entity_model.InventoryModel;
import asksef.entity.entity_model.ItemModel;
import asksef.entity.entity_model.StoreModel;
import asksef.entity.repository.InventoryRepository;
import asksef.entity.service.InventoryService;
import lombok.NonNull;
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
@RequestMapping(value = "/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final InventoryRepository inventoryRepository;
    private final InventoryModelAssemblerSupport modelAssemblerSupport;

    public InventoryController(InventoryService service, InventoryRepository inventoryRepository,
                               InventoryModelAssemblerSupport modelAssemblerSupport) {
        this.inventoryService = service;
        this.inventoryRepository = inventoryRepository;
        this.modelAssemblerSupport = modelAssemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<InventoryModel>> all() {
        List<Inventory> entityModelList = inventoryService.findAll().stream().toList();
        log.info("InventoryController allInventories");
        return new ResponseEntity<>(this.modelAssemblerSupport
                .toCollectionModel(entityModelList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<InventoryModel> one(@PathVariable("id") Long id) {
        //Inventory inventory =
        return this.inventoryRepository.findById(id)
                .map(this.modelAssemblerSupport::toModel)
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
        @NonNull InventoryModel entityModel = this.modelAssemblerSupport.toModel(savedInventory);
        log.info("InventoryController addInventory");
        //
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Inventory inventory) {
        Inventory updateInventory = inventoryService.update(id, inventory);

        @NonNull InventoryModel entityModel = this.modelAssemblerSupport.toModel(updateInventory);
        log.info("InventoryController updateInventory");
        //return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(InvoiceController.class).one(id))
                        .toUri()).body(entityModel);
    }

    @GetMapping(value = "{id}/item", produces = "application/hal+json")
    public ResponseEntity<ItemModel> findItemOfInventory(@PathVariable("id") Long id) {
        Item item =this.inventoryService.findItemOfInventory(id);
        //  build model
        ItemModel itemModel = new ItemModelAssemblerSupport().toModel(item);
//        ItemModel.builder()
//                .itemId(item.getItemId())
//                .itemCost(item.getItemCost())
//                .itemDesc(item.getItemDesc())
//                .itemName(item.getItemName())
//                .saleInfo(item.getSaleInfo())
//                .lastUpdate(item.getLastUpdate())
//               // .inventoryModels(item.getInventoryList())                .build();
        itemModel.add(linkTo(methodOn(InventoryController.class).findItemOfInventory(id)).withSelfRel());
               return new ResponseEntity<>(itemModel, HttpStatus.OK);
    }

    @GetMapping(value = "{id}/store", produces = "application/hal+json")
    public ResponseEntity<StoreModel> findStoreOfInventory(@PathVariable("id") Long id) {
        Store store = this.inventoryService.findStoreOfInventory(id);
        //  build model
        StoreModel storemodel = new StoreModelAssemblerSupport().toModel(store);
//                StoreModel.builder()
//                .storeName(store.getStoreName())
//                .storeId(store.getStoreId())
//                .build();
        storemodel.add(linkTo(methodOn(InventoryController.class).findStoreOfInventory(id)).withRel("store of Inventory"));
//        storemodel.add(linkTo(methodOn(InventoryController.class).one(id)).withRel("inventory"));
//        storemodel.add(linkTo(methodOn(StoreController.class).one(id)).withRel("store"));
        return new ResponseEntity<>(storemodel, HttpStatus.OK);
    }
}
