package asksef.assembler_support;

import asksef.controller.AddressController;
import asksef.entity.Address;
import asksef.entity.model.AddressModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class AddressModelAssemblerSupport extends RepresentationModelAssemblerSupport<Address, AddressModel> {
    public AddressModelAssemblerSupport() {
        super(AddressController.class, AddressModel.class);
    }

    @NonNull
    @Override
    public AddressModel toModel(@NonNull Address entity) {
        AddressModel addressModel = instantiateModel(entity);
        addressModel.setAddressId(entity.getAddressId());
        addressModel.setGpsCode(entity.getGpsCode());
        addressModel.setPhone(entity.getPhone());
        addressModel.setCity(entity.getCity());
        return addressModel;
    }

    @NonNull
    @Override
    public CollectionModel<AddressModel> toCollectionModel(@NonNull Iterable<? extends Address> entities) {
        CollectionModel<AddressModel> addressModels = super.toCollectionModel(entities);
        addressModels.add(linkTo(methodOn(AddressController.class).all()).withSelfRel());
        return addressModels;
    }
}
