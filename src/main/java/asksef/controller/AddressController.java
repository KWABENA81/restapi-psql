package asksef.controller;

import asksef.assembler.AddressModelAssembler;
import asksef.entity.Address;
import asksef.entity.service.AddressService;
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
@RequestMapping(value = "/api/address")
public class AddressController {
    private static final Logger log = LoggerFactory.getLogger(AddressController.class);

    private final AddressService addressService;
    private final AddressModelAssembler addressModelAssembler;

    public AddressController(AddressService addressServ, AddressModelAssembler addressModelAssembler) {
        this.addressService = addressServ;
        this.addressModelAssembler = addressModelAssembler;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Address>> all() {
        List<EntityModel<Address>> entityModelList = addressService.findAll().
                stream().map(addressModelAssembler::toModel).collect(Collectors.toList());
        return CollectionModel.of(entityModelList,
                linkTo(methodOn(AddressController.class).all()).withSelfRel());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Address> one(@PathVariable("id") Long id) {
        Address address = addressService.findById(id);
        return addressModelAssembler.toModel(address);
    }

    @GetMapping("/ph")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<Address> findByPhone(@RequestParam(value = "nr") String phone) {
        Address address = addressService.findByPhone(phone);
        return this.addressModelAssembler.toModel(address);
    }

    @GetMapping("/loc")
    @ResponseStatus(HttpStatus.OK)
    public CollectionModel<EntityModel<Address>> findByLocation(@RequestParam(value = "code") String code) {
        List<EntityModel<Address>> modelList = this.addressService.findByCode(code)
                .stream().map(addressModelAssembler::toModel).toList();

        return CollectionModel.of(modelList,
                linkTo(methodOn(AddressController.class).findByLocation(code)).withSelfRel());
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        this.addressService.delete(id);
        return new ResponseEntity<>("Address entity deleted", HttpStatus.NO_CONTENT);
    }


    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> add(@RequestBody Address address) {
        Address savedAddress = addressService.save(address);
        EntityModel<Address> entityModel = addressModelAssembler.toModel(savedAddress);
        log.info("Address saved: {}", entityModel);
//        return ResponseEntity
//                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
//                .body(entityModel);
        return ResponseEntity
                .created(linkTo(methodOn(AddressController.class).one(savedAddress.getAddressId()))
                        .toUri()).body(entityModel);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Address newAddress) {
        Address updatedAddress = addressService.update(id, newAddress);
        EntityModel<Address> entityModel = addressModelAssembler.toModel(updatedAddress);
        log.info("Updated address: {}", entityModel);
//        return ResponseEntity.
//                created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity.
                created(linkTo(methodOn(AddressController.class).one(id))
                        .toUri()).body(entityModel);
    }

}
