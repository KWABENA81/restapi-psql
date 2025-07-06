package asksef.controller;

import com.asksef.entity.core.Country;
import com.asksef.entity.service_impl.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureWebClient
public class CountryControllerUnitTest {

    private final Country countryCanada = Country.builder().id(66L).country("Canada").lastUpdate(LocalDateTime.now()).build();
    private final Country countryNaija = Country.builder().id(86L).country("Nigeria").lastUpdate(LocalDateTime.now()).build();
    private final List<Country> testCountryList = Arrays.asList(countryCanada, countryNaija);

    //  bind port
    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

//    @Autowired
//    private WebApplicationContext webAppContext;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockitoBean
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        //port = 8088;
        // mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
    }

    @AfterEach
    void tearDown() {
    }

//    @Test
//    public void testCountry_All_Endpoint() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/country/all"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }


    //    @Test
//    void oneCountry() throws Exception {
//        Country country = this.testCountryList
//                .get(new Random().nextInt(this.testCountryList.size()));
//log.info("country:{},  {}", country,(country==null));
//        Assertions.assertNotNull(country);
//        Long id = country.getCountryId();
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/country/{id}", id)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.countryId").value(id));
//    }
    @Disabled
    @Test
    void delete() {
    }

    //    @Test
//    void addCountry() throws Exception {
//        ObjectMapper objectMapper = new ObjectMapper();
//        Country newCountry = this.testCountryList
//                .get(new Random().nextInt(this.testCountryList.size()));
//        mockMvc.perform(MockMvcRequestBuilders.post("/country")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newCountry))
//                        .accept(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk());
//        //    .andExpect(model().attributeHasErrors("country"));
//    }
    @Disabled
    @Test
    void update() {
    }


    //    @Test
//    public void testCountry_Update_Endpoint() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.put("/country/update"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
    @Disabled
    @Test
    public void whenPostRequestToAddCountry_thenCorrectResponse() throws Exception {
        Country mockCountry = //Country.builder().id(77L).country("Senegal").lastUpdate(LocalDateTime.now()).build();
                testCountryList.get(new Random().nextInt(this.testCountryList.size()));
        ResponseEntity<Country> response = restTemplate.postForEntity("/country/add", mockCountry, Country.class);

        mockMvc.perform(MockMvcRequestBuilders.post("/country/add"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getCountryId());
    }
}