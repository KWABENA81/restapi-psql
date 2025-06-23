package asksef.assembler;

import asksef.controller.AddressController;
import asksef.entity.Address;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NonNullApi
public class AddressModelAssembler implements RepresentationModelAssembler<Address, EntityModel<Address>> {
    @Override
    public EntityModel<Address> toModel(Address address) {
        try {
            return EntityModel.of(address,
                    linkTo(methodOn(AddressController.class).one(address.getAddressId())).withSelfRel(),
                    linkTo(methodOn(AddressController.class).findByLocation(address.getGpsCode())).withSelfRel(),
                    linkTo(methodOn(AddressController.class).findByPhone(address.getPhone())).withSelfRel(),
                    linkTo(methodOn(AddressController.class).delete(address.getAddressId())).withSelfRel(),
                    linkTo(methodOn(AddressController.class).update(address.getAddressId(), address)).withSelfRel(),
                    linkTo(methodOn(AddressController.class).add(address)).withSelfRel(),
                    linkTo(methodOn(AddressController.class).all()).withRel("all"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}