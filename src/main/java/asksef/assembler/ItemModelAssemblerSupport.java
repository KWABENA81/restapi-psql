package asksef.assembler;

import asksef.controller.ItemController;
import asksef.entity.core.Item;
import asksef.entity.model.ItemModel;
import lombok.NonNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ItemModelAssemblerSupport extends RepresentationModelAssemblerSupport<Item, ItemModel> {
    public ItemModelAssemblerSupport() {
        super(ItemController.class, ItemModel.class);
    }

    @NonNull
    @Override
    public ItemModel toModel(@NonNull Item item) {
        ItemModel itemModel = instantiateModel(item);
        itemModel.add(linkTo(methodOn(ItemController.class).all()).withRel("all"));
        itemModel.add(linkTo(methodOn(ItemController.class).one(item.getItemId())).withRel("one"));
        itemModel.add(linkTo(methodOn(ItemController.class).itemByCode(item.getItemCode())).withRel("Item Code"));
        itemModel.add(linkTo(methodOn(ItemController.class).itemByNameLike(item.getItemName())).withRel("Item Name"));
        itemModel.add(linkTo(methodOn(ItemController.class).itemByDescLike(item.getItemDesc())).withRel("Item Desc"));
//      itemModel.add(linkTo(methodOn(ItemController.class).all()).withRel("all"));
        return itemModel;
    }

    @NonNull
    @Override
    public CollectionModel<ItemModel> toCollectionModel(@NonNull Iterable<? extends Item> items) {
        CollectionModel<ItemModel> itemModels = super.toCollectionModel(items);
        itemModels.add(linkTo(methodOn(ItemController.class).all()).withRel("all"));

        return itemModels;
    }
}
