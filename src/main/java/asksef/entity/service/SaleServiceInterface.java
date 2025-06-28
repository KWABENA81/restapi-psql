package asksef.entity.service;


import asksef.entity.Sale;

import java.util.Collection;

public interface SaleServiceInterface {
    Collection<Sale> findAll();

    Collection<Sale> findAll(int pageNumber, int pageSize);

    Sale findById(Long id);

    Sale save(Sale sale);

    Sale update(Sale sale);

    void delete(Sale sale);

    Long count();
}
