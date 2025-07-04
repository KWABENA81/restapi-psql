package asksef.controller;

import asksef.assembler.AddressModelAssemblerSupport;
import asksef.assembler.CityModelAssemblerSupport;
import asksef.entity.Address;
import asksef.entity.City;
import asksef.entity.entity_model.AddressModel;
import asksef.entity.entity_model.CityModel;
import asksef.entity.repository.AddressRepository;
import asksef.entity.service.AddressService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
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
    private final AddressModelAssemblerSupport addressModelAssemblerSupport;

    public AddressController(AddressService addressServ, AddressRepository addressRepository,
                             AddressModelAssemblerSupport addressModelAssemblerSupport) {
        this.addressService = addressServ;
        this.addressRepository = addressRepository;
        this.addressModelAssemblerSupport = addressModelAssemblerSupport;
    }

//    @GetMapping(value = "/all", produces = "application/hal+json")
//    @ResponseStatus(HttpStatus.OK)
//    public CollectionModel<EntityModel<Address>> all() {
//        List<EntityModel<Address>> entityModelList = addressService.findAll().
//                stream().map(addressModelAssembler::toModel).collect(Collectors.toList());
//        return CollectionModel.of(entityModelList,
//                linkTo(methodOn(AddressController.class).all()).withSelfRel());
//    }

    @GetMapping(value = "/all", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<CollectionModel<AddressModel>> all() {
        Collection<Address> addressCollection = addressService.findAll();

        CollectionModel<AddressModel> addressCollectionModels
                = this.addressModelAssemblerSupport.toCollectionModel(addressCollection);
        log.debug("All addresses addressCollectionModels: {}", addressCollectionModels);
        return new ResponseEntity<>(addressCollectionModels, HttpStatus.OK);
    }

    //    @GetMapping(value = "/{id}", produces = "application/hal+json")
//    @ResponseStatus(HttpStatus.OK)
//    public EntityModel<Address> one(@PathVariable("id") Long id) {
//        Address address = addressService.findById(id);
//        return addressModelAssembler.toModel(address);
//    }
//
    @GetMapping(value = "/{id}", produces = "application/hal+json")
    public ResponseEntity<AddressModel> one(@PathVariable("id") Long id) {
        return this.addressRepository.findById(id)
                .map(addressModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping(value = "{id}/city", produces = "application/hal+json")
    public ResponseEntity<CityModel> findCityOfAddress(@PathVariable("id") Long id) {
        City city = addressService.findCityOfAddress(id);
        //  build a model
        CityModel cityModel =new CityModelAssemblerSupport().toModel(city);
//                CityModel.builder()
//                .cityId(city.getCityId())
//                .city(city.getCity())
//                .country(city.getCountry())
//                .lastUpdate(city.getLastUpdate())
//                .build();
        //  add links
        cityModel.add(linkTo(methodOn(AddressController.class).findCityOfAddress(id)).withSelfRel());
        cityModel.add(linkTo(methodOn(CityController.class).one(id)).withRel("address"));
        return new ResponseEntity<>(cityModel, HttpStatus.OK);
    }


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
    @GetMapping(value = "/ph", produces = "application/hal+json")
    public ResponseEntity<AddressModel> findByPhone(@RequestParam(value = "nr") String phone) {
        Optional<Address> address = addressService.findByPhone(phone);
        return address.map(addressModelAssemblerSupport::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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

    @GetMapping(value = "/loc", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Collection<AddressModel>> findByLocation(@RequestParam(value = "code") String code) {
        Collection<AddressModel> modelList = this.addressService.findByCode(code)
                .stream().map(addressModelAssemblerSupport::toModel)
                .collect(Collectors.toList());

        return new ResponseEntity<>(modelList, HttpStatus.OK);
//                linkTo(methodOn(AddressController.class).findByLocation(code)).withSelfRel());
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

//    @PostMapping(value = "/add", produces = "application/hal+json")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<?> add(@RequestBody Address address) {
//        Address savedAddress = addressService.save(address);
//        EntityModel<Address> entityModel = addressModelAssembler.toModel(savedAddress);
//        log.info("Address saved: {}", entityModel);
////        return ResponseEntity
////                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
////                .body(entityModel);
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
    @PutMapping(value = "/update/{id}", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Address newAddress) {
        Address updatedAddress = addressService.update(id, newAddress);
        AddressModel entityModel = addressModelAssemblerSupport.toModel(updatedAddress);
        log.info("Updated address: {}", entityModel);
//        return ResponseEntity.
//                created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
        return ResponseEntity.
                created(linkTo(methodOn(AddressController.class).update(id, updatedAddress))
                        .toUri()).body(entityModel);
    }
}
