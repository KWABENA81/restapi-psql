package asksef.entity.service;

import asksef.entity.Inventory;
import asksef.entity.repository.InventoryRepository;
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
public class InventoryService implements InventoryServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);
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
        Inventory inventory = inventoryRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Inventory", "id", null, id)
        );
        log.info("find inventory with id: {}", id);
        return inventory;
    }

    @Override
    public Inventory save(Inventory inventory) {
        Optional<Inventory> optional = this.inventoryRepository.findById(inventory.getInventoryId());
        if (optional.isEmpty()) {
            return inventoryRepository.save(inventory);
        } else {
            throw new CustomResourceExistsException("Inventory", "id", null, inventory.getInventoryId());
        }
    }

    @Override
    public Inventory update(Inventory inventory) {
        Long id = inventory.getInventoryId();
        Optional<Inventory> optional = inventoryRepository.findById(id);
        if (optional.isEmpty()) {
            throw new CustomResourceNotFoundException("Inventory", "id", null, id)                    ;
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

}
