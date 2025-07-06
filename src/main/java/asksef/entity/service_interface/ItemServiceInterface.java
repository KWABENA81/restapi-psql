package asksef.entity.service_interface;


import asksef.entity.core.Item;

import java.util.Collection;

public interface ItemServiceInterface {
    Collection<Item> findAll();

    Collection<Item> findAll(int pageNumber, int pageSize);

    Item findById(Long id);

    Item save(Item item);

    Item update(Item item);

    void delete(Item item);

    Long count();
}
