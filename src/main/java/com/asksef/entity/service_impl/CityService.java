package com.asksef.entity.service_impl;

import com.asksef.entity.core.Address;
import com.asksef.entity.core.City;
import com.asksef.entity.core.Country;
import com.asksef.entity.model.CityModel;
import com.asksef.entity.repository.CityRepository;
import com.asksef.errors.CustomResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class CityService implements CityServiceInterface {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public Collection<City> findAll() {
        return cityRepository.findAll();
    }

    @Override
    public Collection<City> findAll(int pageNumber, int pageSize) {
        return cityRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public City findById(Long id) {
        City city = cityRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("city", "id", null, id)
        );
        log.info("Found city with id: {}", id);
        return city;
    }

    @Transactional
    @Override
    public City save(City city) {
        return this.cityRepository.save(city);
    }

    @Transactional
    public City save(@Valid CityModel cityModel) {
        City city = City.builder()
                .id(cityModel.getCityId())
                .city(cityModel.getCity())
                //  .country(cityModel.getCountryModel())
                .lastUpdate(cityModel.getLastUpdate())
                .build();
        return save(city);
//        Optional<City> optionalCity = this.cityRepository.findById(city.getCityId());
//        if (optionalCity.isEmpty()) {
//            log.info("To Save city: {}", city);
//            return cityRepository.save(city);
//        } else
//            throw new CustomResourceExistsException("City", "id", null, city.getCityId());
    }


    @Override
    public City update(City city) {
        Long id = city.getCityId();
        Optional<City> optionalCity = cityRepository.findById(id);
        if (optionalCity.isEmpty()) {
            log.info("To Update city: {}", city);
            throw new CustomResourceNotFoundException("city", "id", null, id);
        } else {
            City _city = optionalCity.get();
            log.info("Updating city");
            _city.setCity(city.getCity());
            _city.setCountry(city.getCountry());
            _city.setAddressList(city.getAddressList());
            return cityRepository.save(_city);
        }
    }

    public City update(Long id, City newCity) {
        if (Objects.equals(id, newCity.getCityId())) {
            return update(newCity);
        } else {
            throw new CustomResourceNotFoundException("City", "id", null, id);
        }
    }

    @Override
    public void delete(City city) {
        delete(city.getCityId());
    }

    @Override
    public void delete(Long id) {
        City _city = cityRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("City", "id", null, id)
        );
        log.info("Deleting city: {}", _city.getCity());
        cityRepository.delete(_city);
    }

    @Override
    public Long count() {
        return cityRepository.count();
    }

    public City findCityByName(String city) {
        log.info("Find city by name: {}", city);
        return cityRepository.findCityByName(city);
    }

    public Country findCountryOfCity(Long id) {
        Optional<City> cityOptional = this.cityRepository.findCountryOfCity(id);
        if (cityOptional.isEmpty()) {
            throw new CustomResourceNotFoundException("city", "id", null, id);
        }
        return cityOptional.get().getCountry();
    }

    public List<Address> findAddressesOfCity(Long cityId) {
        return this.cityRepository.findAddressesOfCity(cityId).stream().toList();
    }
}


//    @Override
//    public CityDTO addCity(CityDTO cityDTO) {
//        City city = new City();
//        Country country = findCountry(cityDTO.getCountry());
//        city.setCountry(country);
//        city.setCity(cityDTO.getCity());
//
//        City savedCity = cityRepository.save(city);
//
//        CityDTO savedCityDTO = new CityDTO();
//        savedCityDTO.setCity(savedCity.getCity());
//        savedCityDTO.setCityId(savedCity.getCityId());
//        savedCityDTO.setCountry(savedCity.getCountry().getCountry());
//        return savedCityDTO;
//    }
//    private Country findCountry(String countryName) {
//        return countryRepository.findCountryByName(countryName);
//    }