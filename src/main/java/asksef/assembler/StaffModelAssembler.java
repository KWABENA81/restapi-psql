package asksef.assembler;

import asksef.controller.StaffController;
import asksef.entity.Staff;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@NonNullApi
public class StaffModelAssembler implements RepresentationModelAssembler<Staff, EntityModel<Staff>> {
    @Override
    public EntityModel<Staff> toModel(Staff staff) {
        return EntityModel.of(staff,
                linkTo(methodOn(StaffController.class).one(staff.getStaffId())).withSelfRel(),
                linkTo(methodOn(StaffController.class).delete(staff.getStaffId())).withSelfRel(),
                linkTo(methodOn(StaffController.class).findByUsername(staff.getUsername())).withSelfRel(),
                linkTo(methodOn(StaffController.class).update(staff.getStaffId(), staff)).withSelfRel(),
                linkTo(methodOn(StaffController.class).add(staff)).withSelfRel(),
//                linkTo(methodOn(StaffController.class).findByNames(staff.ge())).withSelfRel(),
                linkTo(methodOn(StaffController.class).all()).withRel("all"));
    }
}