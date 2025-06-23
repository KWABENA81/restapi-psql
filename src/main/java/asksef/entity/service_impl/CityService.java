package asksef.entity.service_impl;

import asksef.entity.City;
import asksef.entity.repository.CityRepository;
import asksef.entity.service_interface.CityServiceInterface;
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
public class CityService implements CityServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(CityService.class);
    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public Collection<City> findAll() {
        log.info("Find all cities");
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

    @Override
    public City save(City city) {
        Optional<City> optionalCity = this.cityRepository.findById(city.getCityId());
        if (optionalCity.isEmpty()) {
            log.info("To Save city: {}", city);
            return cityRepository.save(city);
        } else
            throw new CustomResourceExistsException("City", "id", null, city.getCityId());
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


    public City findCityByName(String city) {
        log.info("Find city by name: {}", city);
        return cityRepository.findCityByName(city);
    }

}
