package asksef.controller;

import asksef.assembler.ItemModelAssembler;
import asksef.entity.Item;
import asksef.entity.service_impl.ItemService;
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
@RequestMapping(value = "/v0/item")
public class ItemController {
    private static final Logger log = LoggerFactory.getLogger(ItemController.class);

    private final ItemService itemService;
    private final ItemModelAssembler itemModelAssembler;

    public ItemController(ItemService itemService, ItemModelAssembler itemModelAssembler) {
        this.itemService = itemService;
        this.itemModelAssembler = itemModelAssembler;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Item>> all() {
        List<EntityModel<Item>> entityModelList = this.itemService.findAll().stream()
                .map(itemModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(ItemController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Item> one(@PathVariable("id") Long id) {
        Item item = this.itemService.findById(id);
        return itemModelAssembler.toModel(item);
    }


    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        this.itemService.delete(id);
        return new ResponseEntity<>("Item entity deleted successfully.", HttpStatus.NO_CONTENT);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(@RequestBody Item item) {
        Item savedItem = this.itemService.save(item);
        EntityModel<Item> entityModel = itemModelAssembler.toModel(savedItem);
        log.info("Item saved: {}", entityModel);
//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(ItemController.class).one(savedItem.getItemId()))
                        .toUri()).body(entityModel);
    }


    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Item newItem) {
        Item updateItem = this.itemService.update(newItem, id);

        EntityModel<Item> entityModel = itemModelAssembler.toModel(updateItem);
        log.info("Updating item with id: {}", id);
//        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(ItemController.class).one(id))
                        .toUri()).body(entityModel);
    }


    @GetMapping("/code")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Item> itemByCode(@RequestParam(value = "code") String code) {
        Item item = this.itemService.findByCode(code);
        log.info("Item found with code {}", code);
        return itemModelAssembler.toModel(item);
    }

    @GetMapping("/desc")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Item>> itemByDescLike(@RequestParam(value = "desc") String desc) {
        List<EntityModel<Item>> entityModelList = this.itemService.findByDescLike(desc)
                .stream().map(itemModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(ItemController.class).itemByDescLike(desc)).withSelfRel());
    }

    @GetMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Item>> itemByNameLike(@RequestParam(value = "name") String name) {
        List<EntityModel<Item>> entityModelList = this.itemService.findByNameLike(name)
                .stream().map(itemModelAssembler::toModel).collect(Collectors.toList());

        return CollectionModel.of(entityModelList,
                linkTo(methodOn(ItemController.class).itemByNameLike(name)).withSelfRel());
    }
}