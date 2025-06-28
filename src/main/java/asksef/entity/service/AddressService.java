package asksef.entity.service;


import asksef.entity.Address;
import asksef.errors.CustomResourceNotFoundException;
import asksef.entity.repository.AddressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;

@Service
public class AddressService implements AddressServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(AddressService.class);
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

    @Override
    public Address findById(Long id) {
        Address address = addressRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Address", "id", null, id)
        );
        log.info("Found address with id: {}", id);
        return address;
    }

    @Override
    public Address save(Address address) {
        return addressRepository.save(address);
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

    public Address findByPhone(String phone) {
        return addressRepository.findByPhone(phone);
    }
}
