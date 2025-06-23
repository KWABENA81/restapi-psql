package asksef.controller;

import asksef.assembler.InventoryModelAssembler;
import asksef.entity.Inventory;
import asksef.entity.service_impl.InventoryService;
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
@RequestMapping(value = "/v0/inventory")
public class InventoryController {
    private static final Logger log = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryService inventoryService;
    private final InventoryModelAssembler inventoryModelAssembler;

    public InventoryController(InventoryService service, InventoryModelAssembler inventoryModelAssembler) {
        this.inventoryService = service;
        this.inventoryModelAssembler = inventoryModelAssembler;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Inventory>> all() {
        List<EntityModel<Inventory>> entityModelList = inventoryService.findAll()
                .stream().map(inventoryModelAssembler::toModel).collect(Collectors.toList());
        log.info("InventoryController allInventories");
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(InventoryController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Inventory> one(@PathVariable("id") Long id) {
        Inventory inventory = this.inventoryService.findById(id);
        return this.inventoryModelAssembler.toModel(inventory);
    }

    @DeleteMapping("/delete/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.inventoryService.delete(id);

        return new ResponseEntity<>("Inventory deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(@RequestBody Inventory inventory) {
        Inventory savedInventory = this.inventoryService.save(inventory);
        EntityModel<Inventory> entityModel = this.inventoryModelAssembler.toModel(savedInventory);
        log.info("InventoryController addInventory");
        //return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(InvoiceController.class).one(savedInventory.getInventoryId()))
                        .toUri()).body(entityModel);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Inventory inventory) {
        Inventory updateInventory = inventoryService.update(id, inventory);

        EntityModel<Inventory> entityModel = this.inventoryModelAssembler.toModel(updateInventory);
        log.info("InventoryController updateInventory");
        //return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(InvoiceController.class).one(id))
                        .toUri()).body(entityModel);
    }

}
