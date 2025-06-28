package asksef.entity.service;


import asksef.entity.Store;
import asksef.entity.repository.StoreRepository;
import asksef.errors.CustomResourceExistsException;
import asksef.errors.CustomResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class StoreService implements StoreServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(StoreService.class);
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

    @Override
    public Store findById(Long id) {
        Store store = this.storeRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Store", "id", null, id)
        );
        log.info("Found store with id: {}", id);
        return store;
    }

    @Override
    public Store save(Store store) {
        Optional<Store> optional = this.storeRepository.findById(store.getStoreId());

        if (optional.isPresent()) {
            throw new CustomResourceExistsException("Store", "id", null, store.getStoreId());
        }
        return this.storeRepository.save(store);
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
}
