package asksef.entity.service;


import asksef.entity.Inventory;

import java.util.Collection;

public interface InventoryServiceInterface {
    Collection<Inventory> findAll();

    Collection<Inventory> findAll(int pageNumber, int pageSize);

    Inventory findById(Long id);

    Inventory save(Inventory inventory);

    Inventory update(Inventory inventory);

    void delete(Inventory inventory);

    Long count();
}
