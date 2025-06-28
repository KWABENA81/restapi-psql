package asksef.entity.service;

import asksef.entity.Sale;
import asksef.entity.repository.SaleRepository;
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
public class SaleService implements SaleServiceInterface {
    private static final Logger log = LoggerFactory.getLogger(SaleService.class);
    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public Collection<Sale> findAll() {
        log.info("Finding all sales...");
        return this.saleRepository.findAll();
    }

    @Override
    public Collection<Sale> findAll(int pageNumber, int pageSize) {
        return this.saleRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public Sale findById(Long id) {
        Sale sale = this.saleRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Sale", "id", null, id)
        );
        log.info("Found Sale with ID: {}", id);
        return sale;
    }

    @Override
    public Sale save(Sale sale) {
        Optional<Sale> optional = this.saleRepository.findById(sale.getSaleId());
        if (optional.isPresent()) {
            throw new CustomResourceExistsException("Sale", "id", null, optional.get().getSaleId());
        }
        return saleRepository.save(sale);
    }

    @Override
    public Sale update(Sale sale) {
        Long id = sale.getSaleId();
        Optional<Sale> optional = this.saleRepository.findById(id);
        if (optional.isPresent()) {
            Sale updateSale = optional.get();
            updateSale.setSaleDate(sale.getSaleDate());
            updateSale.setSaleNr(sale.getSaleNr());

            updateSale.setInvoice(sale.getInvoice());
            updateSale.setStaff(sale.getStaff());

            log.info("Updating Sale {}", updateSale.getSaleNr());
            return saleRepository.save(updateSale);
        } else {
            throw new CustomResourceNotFoundException("Sale", "id", null, id);
        }

    }

    public Sale update(Long id, Sale newSale) {
        if (Objects.equals(newSale.getSaleId(), id)) {
            return update(newSale);
        } else
            throw new CustomResourceNotFoundException("Country", "id", null, id);
    }

    @Override
    public void delete(Sale sale) {
        delete(sale.getSaleId());
    }

    public void delete(Long id) {
        Sale sale = this.saleRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Sale", "id", null, id)
        );
        log.info("Deleting country id: {}", id);
        this.saleRepository.delete(sale);
    }

    public void delete(String saleNr) {
        Sale sale = findBySaleNr(saleNr);
        if (sale != null) {
            log.info("Deleting Sale# {}", sale);
            delete(sale.getSaleId());
        }
    }

    @Override
    public Long count() {
        log.info("Counting Sales ...");
        return this.saleRepository.count();
    }

    public Sale findBySaleNr(String nr) {
        return this.saleRepository.findBySaleNr(nr);
    }
}
