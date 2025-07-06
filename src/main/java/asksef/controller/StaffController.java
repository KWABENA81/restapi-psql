package asksef.controller;

import asksef.assembler.StaffModelAssemblerSupport;
import asksef.entity.core.Staff;
import asksef.entity.model.StaffModel;
import asksef.entity.repository.StaffRepository;
import asksef.entity.service_impl.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@RequestMapping(value = "/api/staff")
public class StaffController {
    private final StaffRepository staffRepository;
    private final StaffService staffService;
    private final StaffModelAssemblerSupport staffModelAssemblerSupport;

    public StaffController(StaffRepository staffRepository, StaffService staffService,
                           StaffModelAssemblerSupport staffModelAssemblerSupport) {
        this.staffRepository = staffRepository;
        this.staffService = staffService;
        this.staffModelAssemblerSupport = staffModelAssemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<StaffModel>> all() {
        List<Staff> entityList = this.staffService.findAll().stream().toList();
        return new ResponseEntity<>(this.staffModelAssemblerSupport.toCollectionModel(entityList), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<StaffModel> one(@PathVariable Long id) {
        return this.staffRepository.findById(id)
                .map(staffModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<StaffModel> findByUsername(@RequestParam(name = "username") String username) {
        Staff staff = this.staffService.findByUsername(username);
        log.info("Fetching staff : {}", staff);
        return new ResponseEntity<>(staffModelAssemblerSupport.toModel(staff), HttpStatus.FOUND);
    }


    @PostMapping(value = "/add", produces = "application/hal+json")
    public ResponseEntity<StaffModel> add(@RequestBody StaffModel staffModel) {
        Staff savedStaff = this.staffService.save(staffModel);
        StaffModel staffEntityModel = staffModelAssemblerSupport.toModel(savedStaff);
        log.info("staff saved: {}", staffEntityModel);
        return new ResponseEntity<>(staffEntityModel, HttpStatus.CREATED);
    }

    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    public ResponseEntity<StaffModel> update(@PathVariable Long id, @RequestBody Staff newStaff) {
        Staff updateStaff = this.staffService.update(id, newStaff);
        StaffModel entityModel = staffModelAssemblerSupport.toModel(updateStaff);
        log.info("Staff updated: {}", updateStaff);

        return new ResponseEntity<>(entityModel, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        this.staffService.delete(id);
        log.info("Deleted staff: {}", id);
        return new ResponseEntity<>("Staff entity deleted successfully.", HttpStatus.NO_CONTENT);
    }
}
//    @GetMapping(value = "/{id}/address", produces = "application/hal+json")
//    public ResponseEntity<AddressModel> findAddressOfStaff(@PathVariable("id") Long id) {
//        Address address = this.staffService.findAddressOfCustomer(id);
//        //  build address model
//        return getAddressModelResponseEntity(id, address);
//    }

//    static ResponseEntity<AddressModel> getAddressModelResponseEntity(@PathVariable("id") Long id, Address address) {
//        AddressModel addressModel = new AddressModelAssemblerSupport().toModel(address);
//        //addressModel.add(linkTo(methodOn(CustomerController.class).findAddressOfCustomer(id)).withRel("Address of Customer"));
//        addressModel.add(linkTo(methodOn(CustomerController.class).one(id)).withRel("customer"));
//        return new ResponseEntity<>(addressModel, HttpStatus.OK);
//    }




//    public CollectionModel<EntityModel<Staff>> findByNames(@PathVariable String names) {
//        List<EntityModel<Staff>> entityModelList = staffRepository.findByNames(names).
//                stream().map(staffModelAssembler::toModel).collect(Collectors.toList());
//        log.info("Fetching staff : {}", entityModelList);
//        return CollectionModel.of(entityModelList,
//                linkTo(methodOn(StaffController.class)
//                        .findByNames(names)).withSelfRel());
//    }