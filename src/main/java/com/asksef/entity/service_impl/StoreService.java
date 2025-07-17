package com.asksef.entity.service_impl;


import com.asksef.entity.core.Address;
import com.asksef.entity.core.Inventory;
import com.asksef.entity.core.Staff;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.StoreModel;
import com.asksef.entity.repository.StoreRepository;
import com.asksef.entity.service_interface.StoreServiceInterface;
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
public class StoreService implements StoreServiceInterface {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Override
    public Collection<Store> findAll() {
        log.info("Find all stores");
        return this.storeRepository.findAll();
    }

    @Override
    public Collection<Store> findAll(int pageNumber, int pageSize) {
        return this.storeRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    public Page<Store> findAll(Pageable pageable) {
        return this.storeRepository.findAll(pageable);
    }

    @Override
    public Store findById(Long id) {
        Store store = this.storeRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Store", "id", null, id)
        );
        log.info("Found store with id: {}", id);
        return store;
    }

    @Transactional
    @Override
    public Store save(Store store) {
        return this.storeRepository.save(store);
    }

    @Transactional
    public Store save(@Valid StoreModel storemodel) {
        Store store = Store.builder()
                .storeName(storemodel.getStoreName())
                .staff(storemodel.getStaff())
                .address(storemodel.getAddress())
                .lastUpdate(storemodel.getLastUpdate())
                .build();
        return save(store);
    }

    @Override
    public Store update(Store store) {
        Long id = store.getStoreId();
        Optional<Store> optional = this.storeRepository.findById(id);

        if (optional.isPresent()) {
            Store store1 = optional.get();
            store1.setStoreName(store.getStoreName());
            store1.setStaff(store.getStaff());
            store1.setAddress(store.getAddress());

            store1.setInventoryList(store.getInventoryList());
            log.info("Updated store with id: {}", store.getStoreId());
            return storeRepository.save(store1);
        }
        throw new CustomResourceNotFoundException("Store", "id", null, id);
    }

    public Store update(Long id, Store store) {
        if (Objects.equals(store.getStoreId(), id)) {
            return update(store);
        } else
            throw new CustomResourceNotFoundException("Store", "id", null, id);
    }

    @Override
    public void delete(Store store) {
        delete(store.getStoreId());
    }

    public void delete(Long id) {
        Store store = this.storeRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Store", "id", null, id)
        );
        this.storeRepository.delete(store);
    }

    @Override
    public Long count() {
        return storeRepository.count();
    }

    public Collection<Store> findByStoreName(String name) {
        return this.storeRepository.findByStoreName(name);
    }

    public Address findStoreAddress(Long id) {
        Optional<Address> addressOptional = this.storeRepository.findAddressOfStore(id);
        if (addressOptional.isEmpty()) {
            throw new CustomResourceNotFoundException("Store", "id", null, id);
        }
        return addressOptional.get();
    }

    public Staff findStoreStaff(Long id) {
        Optional<Staff> staffOptional = this.storeRepository.findStaffOfStore(id);
        if (staffOptional.isEmpty()) {
            throw new CustomResourceNotFoundException("Store", "id", null, id);
        }
        return staffOptional.get();
    }

    public List<Inventory> findStoreInventories(Long id) {
        Store store = this.storeRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Store", "id", null, id)
        );
        return store.getInventoryList();
    }
}
