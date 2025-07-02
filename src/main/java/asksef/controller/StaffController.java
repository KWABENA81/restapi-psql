package asksef.controller;

import asksef.assembler.StaffModelAssembler;
import asksef.entity.Staff;
import asksef.entity.service.StaffService;
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
@RequestMapping(value = "/api/staff")
public class StaffController {

    private static final Logger log = LoggerFactory.getLogger(StaffController.class);
    private final StaffService staffService;
    private final StaffModelAssembler staffModelAssembler;

    public StaffController(StaffService staffService, StaffModelAssembler staffModelAssembler) {
        this.staffService = staffService;
        this.staffModelAssembler = staffModelAssembler;
        log.info("StaffController created");
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Staff>> all() {
        List<EntityModel<Staff>> entityModelList
                = this.staffService.findAll().stream().
                map(staffModelAssembler::toModel).collect(Collectors.toList());
        log.info("All staffs found {}", entityModelList);
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(StaffController.class).all()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Staff> one(@PathVariable Long id) {
        Staff staff = this.staffService.findById(id);
        return staffModelAssembler.toModel(staff);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Staff> findByUsername(@RequestParam(name = "username") String username) {
        Staff staff = this.staffService.findByUsername(username);
        log.info("Fetching staff : {}", staff);
        return staffModelAssembler.toModel(staff);
    }

    /// /////////////////////////
    @PostMapping(value = "/add", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(@RequestBody Staff staff) {
        Staff savedStaff = this.staffService.save(staff);
        EntityModel<Staff> staffEntityModel = staffModelAssembler.toModel(savedStaff);
        log.info("staff saved: {}", staffEntityModel);
//        return ResponseEntity.created(staffEntityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(staffEntityModel);
        return ResponseEntity
                .created(linkTo(methodOn(StaffController.class).one(savedStaff.getStaffId()))
                        .toUri()).body(staffEntityModel);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Staff newStaff) {
        Staff updateStaff = this.staffService.update(id, newStaff);

        EntityModel<Staff> entityModel = staffModelAssembler.toModel(updateStaff);
        log.info("Staff updated: {}", updateStaff);
//        return ResponseEntity//                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF)//                .toUri()).body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(StaffController.class).one(id))
                        .toUri()).body(entityModel);
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.staffService.delete(id);
        log.info("Deleted staff: {}", id);
        return new ResponseEntity<>("Staff entity deleted successfully.", HttpStatus.NO_CONTENT);
    }
//    public CollectionModel<EntityModel<Staff>> findByNames(@PathVariable String names) {
//        List<EntityModel<Staff>> entityModelList = staffRepository.findByNames(names).
//                stream().map(staffModelAssembler::toModel).collect(Collectors.toList());
//        log.info("Fetching staff : {}", entityModelList);
//        return CollectionModel.of(entityModelList,
//                linkTo(methodOn(StaffController.class)
//                        .findByNames(names)).withSelfRel());
//    }
}
