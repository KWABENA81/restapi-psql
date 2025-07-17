package asksef.entity.service_impl;

import com.asksef.entity.core.Country;
import com.asksef.entity.repository.CountryRepository;
import com.asksef.entity.service_impl.CountryService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


@SpringBootTest
public class CountryServiceUnitTest {

    private final Country countryCanada = Country.builder()
            // .countryId(16L)
            .lastUpdate(LocalDateTime.now())
            .country("Canada")
            .build();
    private final Country countryNaija = Country.builder()
            // .countryId(23L)
            .lastUpdate(LocalDateTime.now())
            .country("Nigeria")
            .build();
    private final List<Country> testCountryList = Arrays.asList(countryCanada, countryNaija);

    @MockitoBean
    CountryRepository countryRepository;

    @Autowired
    CountryService countryService;


    @BeforeEach
    public void setUp() {
        Mockito.mockitoSession().initMocks(CountryServiceUnitTest.class);
    }

    @AfterEach
    public void tearDown() {
    }
//    @Test
//    void testCountryService_save() {
//        Long id = 130L;
//        Country mockCountry = new Country("North West");
//        mockCountry.setCountryId(id);
//
//        Mockito.when(countryRepository.save(Mockito.any(Country.class)))
//                .thenReturn(new Country());
//        Country savedCountry = countryService.save(mockCountry);
//        Assertions.assertNotNull(savedCountry);
//    }

//    @Test
//    void testCountryService_findAll() {
//        countryRepository.findAll();
//        Mockito.when(countryRepository.findAll()).thenReturn(testCountryList);
//        Mockito.verify(countryRepository, times(1)).findAll();
//        //
//        List<Country> countries = new ArrayList<>(countryService.findAll());
//
//        Assertions.assertEquals(testCountryList, countries);
//        Assertions.assertEquals(testCountryList.size(), countries.size());
//    }


//    @Test
//    void findCountryByName() {
//        Country mockCountry = this.testCountryList.get(new Random().nextInt(this.testCountryList.size()));
//        String countryName = mockCountry.getCountry();
//        when(countryRepository.findCountryByName(countryName)).thenReturn(mockCountry);
//
//        Country qCountry = this.countryService.findCountryByName(countryName);
//
//        assertThat(qCountry).isNotNull();
//        assertThat(qCountry.getCountry()).isEqualTo(countryName);
//        assertThat(qCountry.getCountryId()).isEqualTo(mockCountry.getCountryId());
//    }

//    @Test
//    void testCountryFindById() {
//        Country mockCountry = this.testCountryList.get(new Random().nextInt(this.testCountryList.size()));
//        Long id = mockCountry.getCountryId();
//
//        //  test behaviour of repository to return mock country
//        when(this.countryRepository.findById(id)).thenReturn(Optional.of(mockCountry));
//
//        Country country = countryService.findById(id);
//
//        //  Assert
//        assertNotNull(country);
//        assertEquals(id, country.getCountryId());
//        assertEquals(mockCountry.getCountryId(), country.getCountryId());
//        assertEquals(mockCountry.getLastUpdate(), country.getLastUpdate());
//        assertEquals(mockCountry.getCountry(), country.getCountry());
//    }
//
//    @Test
//    void update() {
//    }
}