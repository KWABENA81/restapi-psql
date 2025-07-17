package com.asksef.entity.service_impl;

import ch.qos.logback.core.util.StringUtil;
import com.asksef.entity.core.City;
import com.asksef.entity.core.Country;
import com.asksef.entity.model.CountryModel;
import com.asksef.entity.repository.CountryRepository;
import com.asksef.entity.service_interface.CountryServiceInterface;
import com.asksef.errors.CustomResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CountryService implements CountryServiceInterface {

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

    public Page<Country> findAll(Pageable pageable) {
        return this.countryRepository.findAll(pageable);
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
                .country(countryModel.getCountry())
                .lastUpdate(countryModel.getLastUpdate())
                .build();
        return save(country);
    }

    @Transactional
    @Override
    public Country save(Country country) {
        return this.countryRepository.save(country);
    }

    @Transactional
    public Country update(@NonNull Long id, @NonNull CountryModel countryModel) {
        Country country = countryRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Country", "id", null, id)
        );
        Country updateCountry = Country.builder()
                .country(countryModel.getCountry())
                .lastUpdate(countryModel.getLastUpdate())
                .build();
        if (!StringUtil.isNullOrEmpty(updateCountry.getCountry())) {
            country.setCountry(countryModel.getCountry());
        }
        return update(updateCountry);
    }

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
//        Country country = Country.builder()
//               // .id(countryModel.getCountryId())
//                .country(countryModel.getCountry())
//                .lastUpdate(countryModel.getLastUpdate())
//                .build();
        return this.countryRepository.save(country);
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

    public List<City> findCountryCities(Long id) {
        Country country = this.countryRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Country", "id", null, id)
        );
        return country.getCityList();
    }

}

//        log.info("Find country by id: {}", id);
//        return country;
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