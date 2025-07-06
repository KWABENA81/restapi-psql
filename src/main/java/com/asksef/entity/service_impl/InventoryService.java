package com.asksef.entity.service_impl;

import com.asksef.entity.core.Inventory;
import com.asksef.entity.core.Item;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.InventoryModel;
import com.asksef.entity.repository.InventoryRepository;
import com.asksef.entity.service_interface.InventoryServiceInterface;
import com.asksef.errors.CustomResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class InventoryService implements InventoryServiceInterface {
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Collection<Inventory> findAll() {
        log.info("Find all inventory");
        return inventoryRepository.findAll();
    }

    @Override
    public Collection<Inventory> findAll(int pageNumber, int pageSize) {
        return inventoryRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public Inventory findById(Long id) {
        return inventoryRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Inventory", "id", null, id)
        );
    }

    @Transactional
    @Override
    public Inventory save(Inventory inventory) {
        return inventoryRepository.save(inventory);
    }

    @Transactional
    public Inventory save(@Valid InventoryModel inventoryModel) {
        Inventory inventory = Inventory.builder()
                .inventoryId(inventoryModel.getInventoryId())
                .item(inventoryModel.getItem())
                .store(inventoryModel.getStore())
                .lastUpdate(inventoryModel.getLastUpdate())
                .reorderQty(inventoryModel.getReorderQty())
                .stockQty(inventoryModel.getStockQty())
                .build();
        return save(inventory);
    }

    @Override
    public Inventory update(Inventory inventory) {
        Long id = inventory.getInventoryId();
        Optional<Inventory> optional = inventoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new CustomResourceNotFoundException("Inventory", "id", null, id);
        } else {
            Inventory inventory1 = optional.get();
            inventory1.setStockQty(inventory.getStockQty());
            inventory1.setReorderQty(inventory.getReorderQty());

            inventory1.setItem(inventory.getItem());
            inventory1.setStore(inventory.getStore());
            log.info("Updated inventory with id: {}", inventory.getInventoryId());
            return inventoryRepository.save(inventory1);
        }
    }

    public Inventory update(Long id, Inventory inventory) {
        if (Objects.equals(id, inventory.getInventoryId())) {
            return update(inventory);
        } else {
            throw new CustomResourceNotFoundException("Inventory", "id", null, id);
        }
    }

    @Override
    public void delete(Inventory inventory) {
        delete(inventory.getInventoryId());
    }

    public void delete(Long id) {
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Inventory", "id", null, id)
        );
        log.info("Deleting inventory id: {}", id);
        inventoryRepository.delete(inventory);
    }

    @Override
    public Long count() {
        log.info("Count inventory");
        return inventoryRepository.count();
    }

    public Store findStoreOfInventory(Long id) {
        return this.inventoryRepository.findStoreOfInventory(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Store", "id", null, id)
        );
    }

    public Item findItemOfInventory(Long id) {
        return this.inventoryRepository.findItemOfInventory(id).orElseThrow(
                () -> new CustomResourceNotFoundException("item", "id", null, id)
        );
    }
}
//Inventory inventory = inventoryRepository.findById(id).orElseThrow(
//        () -> new CustomResourceNotFoundException("Inventory", "id", null, id)
//);
//        log.info("find inventory with id: {}", id);
//        return inventory;