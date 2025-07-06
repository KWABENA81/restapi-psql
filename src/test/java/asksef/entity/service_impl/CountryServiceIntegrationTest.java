package asksef.entity.service_impl;

import com.asksef.entity.core.Country;
import com.asksef.entity.repository.CountryRepository;
import com.asksef.entity.service_impl.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CountryServiceIntegrationTest {

    private final Country countryCanada = Country.builder()
            .id(16L).lastUpdate(LocalDateTime.now()).country("Canada").build();
    private final Country countryNaija = Country.builder().id(23L)
            .lastUpdate(LocalDateTime.now()).country("Nigeria").build();
    private final List<Country> testCountryList = Arrays.asList(countryCanada, countryNaija);

    @MockitoBean
    CountryRepository countryRepository;

    @Autowired
    CountryService countryService;

    @Autowired
    private TestRestTemplate testRestTemplate;
//    @Autowired
//    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        Mockito.mockitoSession().initMocks(this);
        // testRestTemplate =new  TestRestTemplate();
    }

    @AfterEach
    void tearDown() {
    }

    //@Disabled
    @Test
    public void whenCountryAdded_thenCorrectResponse() {
        Country mockCountry = this.testCountryList
                .get(new Random().nextInt(this.testCountryList.size()));

        ResponseEntity<Country> response = testRestTemplate
                .postForEntity("/country/add", mockCountry, Country.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//assertNotNull(response.getBody().getCountryId());
    }


//    @Test
//    public void whenCountryAdded_thenControlFlowAsExpected() {
//        Country mockCountry = this.testCountryList.get(new Random().nextInt(this.testCountryList.size()));
//        Mockito.when(this.countryRepository.save(Mockito.any(Country.class))).thenReturn(mockCountry);
//
//        this.countryService.save(mockCountry);
//        Mockito.verify(this.countryRepository, Mockito.times(1)).save(Mockito.any(Country.class));
//    }


//    @Test
//    public void whenCountryUpdated_thenControlFlowAsExpected() {
//        Country updateCountry = this.testCountryList
//                .get(new Random().nextInt(this.testCountryList.size()));
//        when(this.countryRepository.findById(Mockito.any(Long.class)))
//                .thenReturn(Optional.of(updateCountry));
//        when(this.countryRepository.save(Mockito.any(Country.class))).thenReturn(updateCountry);
//        this.countryService.update(new Random().nextLong(this.testCountryList.size()), new Country());
//
//        Mockito.verify(this.countryRepository, Mockito.times(1)).save(Mockito.any(Country.class));
//    }

    @Test
    public void whenCountryDeleted_thenControlFlowAsExpected() {
        Mockito.doNothing().when(this.countryRepository).delete(Mockito.any(Country.class));
        this.countryService.delete(this.testCountryList.get(new Random().nextInt(this.testCountryList.size())));

        Mockito.verify(this.countryRepository, Mockito.times(1)).delete(Mockito.any(Country.class));
    }

    @Disabled
    @Test
    void testFindAll() {
    }

    @Test
    void testCountryService_findById() {
        Long id = 130L;
        Country mockCountry = new Country("North West");
        mockCountry.setCountryId(id);

        // Mock behaviour repository to return the mock object
        Mockito.when(countryRepository.findById(id)).thenReturn(Optional.of(mockCountry));

        // Act
        Country result = countryService.findById(id);
        // Assert
        assertNotNull(result);
        assertEquals(id, result.getCountryId());
        log.info("testCountry {}", result);
    }


    @Test
    void testCountryService_update() {
        Long id = 130L;
        Country mockCountry = new Country("North West");
        mockCountry.setCountryId(id);

        Optional<Country> mockCountryOptional = Optional.of(mockCountry);
        Mockito.when(countryRepository.findById(id)).thenReturn(mockCountryOptional);

        ArgumentCaptor<Country> captor = ArgumentCaptor.forClass(Country.class);
        Mockito.when(countryRepository.save(captor.capture())).thenAnswer(ac -> ac.getArgument(0));

        mockCountry.setCountry("Updated West");
        Country updatedCountry = countryService.update(mockCountry);
        assertNotNull(updatedCountry);
    }

    @Disabled
    @Test
    void testUpdate() {
    }

    @Disabled
    @Test
    void delete() {
    }

    @Disabled
    @Test
    void testDelete() {
    }

    @Disabled
    @Test
    void test_delete_thenDeleteObject() {
        Long id = 130L;
        Country mockCountry = new Country("North West");
        mockCountry.setCountryId(id);
        log.info("to be delete Country {}", mockCountry);

        Optional<Country> optionalMockCountry = Optional.of(mockCountry);
        Mockito.when(countryRepository.findById(id)).thenReturn(optionalMockCountry);
        // when
        countryService.delete(mockCountry);
        // then
        Mockito.verify(countryRepository, times(1)).delete(mockCountry);
    }

//    @Test
//    void testCountryService_count() {
//        Mockito.when(countryRepository.count()).thenReturn((long) countryList.size());
//        long count = countryService.count();
//
//        Assertions.assertEquals(count, countryList.size());
//        Mockito.verify(countryRepository).count();
//    }

    @Test
    void findByName() {
    }

    @Test
    void findLikeName() {
    }
}