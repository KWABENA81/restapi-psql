package com.asksef.entity.service_impl;


import com.asksef.entity.core.Address;
import com.asksef.entity.core.City;
import com.asksef.entity.core.Customer;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.AddressModel;
import com.asksef.entity.repository.AddressRepository;
import com.asksef.entity.service_interface.AddressServiceInterface;
import com.asksef.errors.CustomResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class AddressService implements AddressServiceInterface {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Collection<Address> findAll() {
        log.info("Find all addresses");
        return addressRepository.findAll();
    }

    @Override
    public Collection<Address> findAll(int pageNumber, int pageSize) {
        return addressRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    public Page<Address> findAll(Pageable pageable) {
        return this.addressRepository.findAll(pageable);
    }

    @Override
    public Address findById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Address", "id", null, id)
        );
        log.info("Found address with id: {}", id);
        return address;
    }

    @Transactional
    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
    }

    @Transactional
    public Address save(@Valid AddressModel addressModel) {
        Address address = Address.builder()
                .city(addressModel.getCity())
                .gpsCode(addressModel.getGpsCode())
                .phone(addressModel.getPhone())
                .lastUpdate(addressModel.getLastUpdate())
                .build();
        return save(address);
    }

    @Override
    public Address update(Address address) {
        Long id = address.getAddressId();
        Address _address = addressRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Address", "id", null, id)
        );
        _address.setGpsCode(address.getGpsCode());
        _address.setPhone(address.getPhone());
        _address.setCity(address.getCity());

        _address.setCustomerList(address.getCustomerList());
        //_address.setStaffList(address.getStaffList());
        _address.setStoreList(address.getStoreList());
        return addressRepository.save(_address);
    }

    public Address update(Long id, Address address) {
        if (Objects.equals(address.getAddressId(), id)) {
            return update(address);
        } else {
            throw new CustomResourceNotFoundException("Address", "id", null, id);
        }
    }

    @Override
    public Long count() {
        return addressRepository.count();
    }

    @Override
    public void delete(Address address) {
        Long id = address.getAddressId();
        Address _address = addressRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Address", "id", null, id)
        );
        addressRepository.delete(_address);
        log.info("Deleted address with id: {}", _address.getAddressId());
    }

    public void delete(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Address", "id", null, id)
        );
        addressRepository.delete(address);
    }

    public Collection<Address> findByCode(String code) {
        return addressRepository.findByCode(code);
    }

    public Optional<Address> findByPhone(String phone) {
        return addressRepository.findByPhone(phone);
    }

    public City findAddressCity(Long id) {
        Optional<Address> addressOptional = this.addressRepository.findCityOfAddress(id);
        if (addressOptional.isEmpty()) {
            throw new CustomResourceNotFoundException("address", "id", null, id);
        }
        return addressOptional.get().getCity();
    }


    public List<Store> findAddressStores(Long id) {
        Address address = this.addressRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Country", "id", null, id)
        );
        return address.getStoreList();
    }

    public List<Customer> findAddressCustomers(Long id) {
        Address address = this.addressRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Country", "id", null, id)
        );
        return address.getCustomerList();
    }

}
