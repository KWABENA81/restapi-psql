package asksef.assembler;

import asksef.controller.ItemController;
import asksef.entity.Item;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NonNullApi
public class ItemModelAssembler implements RepresentationModelAssembler<Item, EntityModel<Item>> {
    @Override
    public EntityModel<Item> toModel(Item item) {
        try {
            return EntityModel.of(item,
                    linkTo(methodOn(ItemController.class).one(item.getItemId())).withSelfRel(),
//                    linkTo(methodOn(ItemController.class).add(item)).withSelfRel(),
//                    linkTo(methodOn(ItemController.class).update(item.getItemId(), item)).withSelfRel(),
//                    linkTo(methodOn(ItemController.class).delete(item.getItemId())).withSelfRel(),
                    linkTo(methodOn(ItemController.class).itemByCode(item.getItemCode())).withSelfRel(),
                    linkTo(methodOn(ItemController.class).itemByDescLike(item.getItemDesc())).withSelfRel(),
                    linkTo(methodOn(ItemController.class).itemByNameLike(item.getItemName())).withSelfRel(),
                    linkTo(methodOn(ItemController.class).all()).withRel("all"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
