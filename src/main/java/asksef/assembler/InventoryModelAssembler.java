package asksef.assembler;

import asksef.controller.InventoryController;
import asksef.entity.Inventory;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NonNullApi
public class InventoryModelAssembler implements RepresentationModelAssembler<Inventory, EntityModel<Inventory>> {
    @Override
    public EntityModel<Inventory> toModel(Inventory inventory) {
        try {
            return EntityModel.of(inventory,
                    linkTo(methodOn(InventoryController.class).one(inventory.getInventoryId())).withSelfRel(),
                    linkTo(methodOn(InventoryController.class).add(inventory)).withSelfRel(),
                    linkTo(methodOn(InventoryController.class).delete(inventory.getInventoryId())).withSelfRel(),
                    linkTo(methodOn(InventoryController.class).update(inventory.getInventoryId(), inventory)).withSelfRel(),
                    linkTo(methodOn(InventoryController.class).all()).withRel("all"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
