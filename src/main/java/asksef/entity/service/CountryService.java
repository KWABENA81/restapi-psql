package asksef.entity.service;

import asksef.entity.Country;
import asksef.entity.entity_dto.CountryTransferObj;
import asksef.entity.entity_model.CountryModel;
import asksef.entity.repository.CountryRepository;
import asksef.errors.CustomResourceNotFoundException;
import ch.qos.logback.core.util.StringUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class CountryService implements CountryServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(CountryService.class);
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Collection<Country> findAll() {
        log.info("Find all countries");
        return this.countryRepository.findAll();
    }

    @Override
    public Collection<Country> findAll(int pageNumber, int pageSize) {
        return this.countryRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public Country findById(@NonNull Long id) {
        Country country = this.countryRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Country", "id", null, id)
        );
        log.info("Find country by id: {}", id);
        return country;
    }

    @Transactional
    public Country save(@Valid CountryModel countryModel) {
        Country country = Country.builder()
                .id(countryModel.getCountryId())
                .country(countryModel.getCountry())
                .lastUpdate(countryModel.getLastUpdate())
                .build();
        return this.countryRepository.save(country);
    }

//    @Transactional
//    @Override
//    public Country save(@NonNull Country country) {
//        Optional<Country> existingEntity = this.countryRepository.findById(country.getCountryId());
//        if (existingEntity.isEmpty()) {
//            return this.countryRepository.save(country);
//        } else {
//            throw new CustomResourceExistsException("Country", "id", null, country.getCountryId());
//        }
//    }

    @Transactional
    @Override
    public Country update(@NonNull Country country) {
//        Long id = country.getCountryId();
//        Optional<Country> existingEntity = this.countryRepository.findById(id);
//        if (existingEntity.isEmpty()) {
//            throw new CustomResourceNotFoundException("Country", "id", null, country.getCountryId());
//        } else {
//            Country updateEntity = existingEntity.get();
//            updateEntity.setCountry(country.getCountry());
//            updateEntity.setCityList(country.getCityList());
//
//            log.info("Updating country id: {}", updateEntity.getCountryId());
        return this.countryRepository.save(country);
        //   }
    }

    @Transactional
    public Country update(@NonNull Long id, @NonNull CountryTransferObj countryDto) {
        Country country = countryRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Country", "id", null, id)
        );
        if (!StringUtil.isNullOrEmpty(countryDto.getCountry())) {
            country.setCountry(countryDto.getCountry());
        }
        return update(country);
    }

    @Transactional
    public void delete(@NonNull Long id) {
        Country country = countryRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Country", "id", null, id)
        );
        log.info("Deleting country id: {}", id);
        delete(country);
    }

    @Transactional
    @Override
    public void delete(@NonNull Country country) {
        countryRepository.delete(country);
    }

    @Transactional
    public void delete(@NonNull String countryName) {
        Optional<Country> countryOptional = findByName(countryName);
        countryOptional.ifPresent(country -> delete(country.getCountryId()));
    }

    @Override
    public Long count() {
        return countryRepository.count();
    }

    public Optional<Country> findByName(@NonNull String countryName) {
        log.info("Finding country by name {}", countryName);
        return this.countryRepository.findByName(countryName);
    }

}
