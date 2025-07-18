package com.asksef.controller;

import com.asksef.assembler.AddressModelAssemblerSupport;
import com.asksef.assembler.CityModelAssemblerSupport;
import com.asksef.assembler.CustomerModelAssemblerSupport;
import com.asksef.assembler.StoreModelAssemblerSupport;
import com.asksef.entity.core.*;
import com.asksef.entity.model.*;
import com.asksef.entity.repository.AddressRepository;
import com.asksef.entity.service_impl.AddressService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping(value = "/api/address")
public class AddressController {

    private final AddressService addressService;
    private final AddressRepository addressRepository;
    private final PagedResourcesAssembler<Address> pagedResourcesAssembler;
    private final AddressModelAssemblerSupport addressModelAssemblerSupport;

    public AddressController(AddressService addressServ, AddressRepository addressRepository,
                             AddressModelAssemblerSupport addressModelAssemblerSupport,
                             PagedResourcesAssembler<Address> pagedResourcesAssembler) {
        this.addressService = addressServ;
        this.addressRepository = addressRepository;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
        this.addressModelAssemblerSupport = addressModelAssemblerSupport;
    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CollectionModel<AddressModel>> all() {
        List<Address> addressCollection = addressService.findAll().stream().toList();

        CollectionModel<AddressModel> addressCollectionModels
                = this.addressModelAssemblerSupport.toCollectionModel(addressCollection);
        log.info("All addresses addressCollectionModels: {}", addressCollectionModels);
        return new ResponseEntity<>(addressCollectionModels, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<AddressModel> one(@PathVariable("id") Long id) {
        return this.addressRepository.findById(id)
                .map(addressModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping(value = "/delete/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        this.addressService.delete(id);
        return new ResponseEntity<>("Address entity deleted", HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/add", produces = "application/hal+json")
    public ResponseEntity<AddressModel> add(@RequestBody @Valid AddressModel addressModel) {
        Address savedAddress = addressService.save(addressModel);
        AddressModel entityModel = addressModelAssemblerSupport.toModel(savedAddress);
        log.info("Address saved: {}", entityModel);
        return new ResponseEntity<>(entityModel, HttpStatus.OK);
    }


    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Address newAddress) {
        Address updatedAddress = addressService.update(id, newAddress);
        AddressModel entityModel = addressModelAssemblerSupport.toModel(updatedAddress);
        log.info("Updated address: {}", entityModel);
        return ResponseEntity.
                created(linkTo(methodOn(AddressController.class).update(id, updatedAddress))
                        .toUri()).body(entityModel);
    }

    @GetMapping(value = "{id}/city", produces = "application/hal+json")
    public ResponseEntity<CityModel> findAddressCity(@PathVariable("id") Long id) {
        City city = addressService.findAddressCity(id);
        //  build a model
        CityModel cityModel = new CityModelAssemblerSupport().toModel(city);
        //  add links
        cityModel.add(linkTo(methodOn(AddressController.class).findAddressCity(id)).withSelfRel());
        cityModel.add(linkTo(methodOn(CityController.class).one(id)).withRel("address"));
        return new ResponseEntity<>(cityModel, HttpStatus.OK);
    }


    @GetMapping(value = "/ph", produces = "application/hal+json")
    public ResponseEntity<AddressModel> findByPhone(@RequestParam(value = "nr") String phone) {
        Optional<Address> address = addressService.findByPhone(phone);
        return address.map(addressModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping(value = "/loc", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Collection<AddressModel>> findByLocation(@RequestParam(value = "code") String code) {
        Collection<AddressModel> modelList = this.addressService.findByCode(code)
                .stream().map(addressModelAssemblerSupport::toModel)
                .collect(Collectors.toList());
        return new ResponseEntity<>(modelList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/stores", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<StoreModel>> findAddressStores(@PathVariable("id") Long id) {
        List<Store> storeList = addressService.findAddressStores(id);
        CollectionModel<StoreModel> storeModels = new StoreModelAssemblerSupport().toCollectionModel(storeList);
        return new ResponseEntity<>(storeModels, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/customers", produces = "application/hal+json")
    public ResponseEntity<CollectionModel<CustomerModel>> findAddressCustomers(@PathVariable("id") Long id) {
        List<Customer> customerList = addressService.findAddressCustomers(id);
        @NonNull CollectionModel<CustomerModel> customerModels = new CustomerModelAssemblerSupport().toCollectionModel(customerList);
        return new ResponseEntity<>(customerModels, HttpStatus.OK);
    }
    @GetMapping(value = "/paged")
    public ResponseEntity<PagedModel<AddressModel>> paged(Pageable pageable) {
        Page<Address> entityPage = addressService.findAll(pageable);

        PagedModel<AddressModel> addressModels = pagedResourcesAssembler.toModel(entityPage, addressModelAssemblerSupport);
        return new ResponseEntity<>(addressModels, HttpStatus.OK);
    }
}

//    @GetMapping(value = "/ph", produces = "application/hal+json")
//    public ResponseEntity<AddressModel> findByPhone(@RequestParam(value = "nr") String phone) {
//        Address address = addressService.findByPhone(phone);
//        //  build model
//        AddressModel addressModel = AddressModel.builder()
//                .addressId(address.getAddressId())
//                .gpsCode(address.getGpsCode())
//                .phone(address.getPhone())
//                .city(address.getCity())
//                .lastUpdate(address.getLastUpdate())
//                .customerList(address.getCustomerList())
//                .storeList(address.getStoreList())
//                .build();
//
//        //addressModel.add(linkTo(methodOn(AddressController.class).findByPhone(phone)).withSelfRel());
//        addressModel.add(linkTo(methodOn(AddressController.class).findByPhone(phone)).withRel("phone"));
//        return new ResponseEntity<>(addressModel, HttpStatus.OK);
//    }


//    @GetMapping(value = "/ph", produces = "application/hal+json")
//    @ResponseStatus(HttpStatus.OK)
//    public EntityModel<Address> findByPhone(@RequestParam(value = "nr") String phone) {
//        Address address = addressService.findByPhone(phone);
//        return this.addressModelAssembler.toModel(address);
//    }
//    @GetMapping(produces = "application/hal+json")
//    public ResponseEntity<CountryModel> findByName(@RequestParam(value = "name") String name) {
//        return this.countryService.findByName(name)
//                .map(countryModelAssemblerSupport::toModel)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//    @GetMapping(value = "/{id}", produces = "application/hal+json")
//    @ResponseStatus(HttpStatus.OK)
//    public EntityModel<Address> one(@PathVariable("id") Long id) {
//        Address address = addressService.findById(id);
//        return addressModelAssembler.toModel(address);
//    }
//
//    @GetMapping(value = "/all", produces = "application/hal+json")
//    @ResponseStatus(HttpStatus.OK)
//    public CollectionModel<EntityModel<Address>> all() {
//        List<EntityModel<Address>> entityModelList = addressService.findAll().
//                stream().map(addressModelAssembler::toModel).collect(Collectors.toList());
//        return CollectionModel.of(entityModelList,
//                linkTo(methodOn(AddressController.class).all()).withSelfRel());
//    }

//    @PostMapping(value = "/add", produces = "application/hal+json")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<?> add(@RequestBody Address address) {
//        Address savedAddress = addressService.save(address);
//        EntityModel<Address> entityModel = addressModelAssembler.toModel(savedAddress);
//        log.info("Address saved: {}", entityModel);
/// /        return ResponseEntity
/// /                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
/// /                .body(entityModel);
//        return ResponseEntity
//                .created(linkTo(methodOn(AddressController.class).add(savedAddress))
//                        .toUri()).body(entityModel);
//    }

//    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Address newAddress) {
//        Address updatedAddress = addressService.update(id, newAddress);
//        EntityModel<Address> entityModel = addressModelAssembler.toModel(updatedAddress);
//        log.info("Updated address: {}", entityModel);

/// /        return ResponseEntity.
/// /                created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
//        return ResponseEntity.
//                created(linkTo(methodOn(AddressController.class).update(id, updatedAddress))
//                        .toUri()).body(entityModel);
//    }