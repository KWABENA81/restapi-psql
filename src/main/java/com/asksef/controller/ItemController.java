package com.asksef.controller;

import com.asksef.assembler.InventoryModelAssemblerSupport;
import com.asksef.assembler.ItemModelAssemblerSupport;
import com.asksef.assembler.OrderModelAssemblerSupport;
import com.asksef.entity.core.Inventory;
import com.asksef.entity.core.Item;
import com.asksef.entity.core.Order;
import com.asksef.entity.model.InventoryModel;
import com.asksef.entity.model.ItemModel;
import com.asksef.entity.model.OrderModel;
import com.asksef.entity.repository.ItemRepository;
import com.asksef.entity.service_impl.ItemService;
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
@RequestMapping(value = "/api/item")
public class ItemController {
    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final PagedResourcesAssembler<Item> pagedResourcesAssembler;
    private final ItemModelAssemblerSupport itemModelAssemblerSupport;

    public ItemController(ItemService itemService, ItemRepository itemRepository,
                          PagedResourcesAssembler<Item> pagedResourcesAssembler,
                          ItemModelAssemblerSupport itemModelAssemblerSupport) {
        this.itemService = itemService;
        this.itemRepository = itemRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.itemModelAssemblerSupport = itemModelAssemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<ItemModel>> all() {
        List<Item> entityList = this.itemService.findAll().stream().toList();
        return new ResponseEntity<>(this.itemModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<ItemModel> one(@PathVariable("id") Long id) {
        return this.itemRepository.findById(id)
                .map(itemModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        this.itemService.delete(id);
        return new ResponseEntity<>("Item entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(@RequestBody ItemModel itemModel) {
        Item savedItem = this.itemService.save(itemModel);
        @NonNull ItemModel entityModel = itemModelAssemblerSupport.toModel(savedItem);
        log.info("Item saved: {}", entityModel);
//
        return new ResponseEntity<>(entityModel, HttpStatus.CREATED);
    }


    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Item newItem) {
        Item updateItem = this.itemService.update(newItem, id);

        @NonNull ItemModel entityModel = itemModelAssemblerSupport.toModel(updateItem);
        log.info("Updating item with id: {}", id);
//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(ItemController.class).one(id))
                        .toUri()).body(entityModel);
    }


    @GetMapping(value = "/code", produces = "application/hal+json")
    public ResponseEntity<ItemModel> itemByCode(@RequestParam(value = "code") String code) {
        Item item = this.itemService.findByCode(code);
        log.info("Item found with code {}", code);
        return new ResponseEntity<>(itemModelAssemblerSupport.toModel(item), HttpStatus.OK);
    }

    @GetMapping(value = "/desc", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<ItemModel>> itemByDescLike(@RequestParam(value = "desc") String desc) {
        List<Item> entityList = this.itemService.findByDescLike(desc)
                .stream().toList();
        return new ResponseEntity<>(this.itemModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/name", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<ItemModel>> itemByNameLike(@RequestParam(value = "name") String name) {
        List<Item> entityList = this.itemService.findByNameLike(name).stream().toList();
        return new ResponseEntity<>(this.itemModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/inventories", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<InventoryModel>> findItemInventories(@PathVariable("id") Long id) {
        List<Inventory> inventoryList = itemService.findItemInventories(id);
        CollectionModel<InventoryModel> inventoryModels = new InventoryModelAssemblerSupport().toCollectionModel(inventoryList);
        return new ResponseEntity<>(inventoryModels, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/orders", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<OrderModel>> findItemOrders(@PathVariable("id") Long id) {
        List<Order> orderList = itemService.findItemOrders(id);
        CollectionModel<OrderModel> orderModels = new OrderModelAssemblerSupport().toCollectionModel(orderList);
        return new ResponseEntity<>(orderModels, HttpStatus.OK);
    }

    @GetMapping(value = "/pageable")
    public ResponseEntity<PagedModel<ItemModel>> pageable(Pageable pageable) {
        Page<Item> entityPage = itemService.findAll(pageable);

        PagedModel<ItemModel> pagedModel = pagedResourcesAssembler.toModel(entityPage, itemModelAssemblerSupport);
        return new ResponseEntity<>(pagedModel, HttpStatus.OK);
    }
}