package com.asksef.controller;

import com.asksef.assembler.PaymentModelAssemblerSupport;
import com.asksef.assembler.SaleModelAssemblerSupport;
import com.asksef.assembler.StaffModelAssemblerSupport;
import com.asksef.assembler.StoreModelAssemblerSupport;
import com.asksef.entity.core.Payment;
import com.asksef.entity.core.Sale;
import com.asksef.entity.core.Staff;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.PaymentModel;
import com.asksef.entity.model.SaleModel;
import com.asksef.entity.model.StaffModel;
import com.asksef.entity.model.StoreModel;
import com.asksef.entity.repository.StaffRepository;
import com.asksef.entity.service_impl.StaffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "/{id}/payments", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<PaymentModel>> findStaffPayments(@PathVariable("id") Long id) {
        List<Payment> paymentList = staffService.findStaffPayments(id);
        CollectionModel<PaymentModel> paymentModels = new PaymentModelAssemblerSupport().toCollectionModel(paymentList);
        return new ResponseEntity<>(paymentModels, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/sales", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<SaleModel>> findStaffSales(@PathVariable("id") Long id) {
        List<Sale> salesList = staffService.findStaffSales(id);
        CollectionModel<SaleModel> saleModels = new SaleModelAssemblerSupport().toCollectionModel(salesList);
        return new ResponseEntity<>(saleModels, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/stores", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<StoreModel>> findStaffStores(@PathVariable("id") Long id) {
        List<Store> storeList = staffService.findStaffStores(id);
        CollectionModel<StoreModel> storeModels = new StoreModelAssemblerSupport().toCollectionModel(storeList);
        return new ResponseEntity<>(storeModels, HttpStatus.OK);
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