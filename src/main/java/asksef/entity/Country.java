package asksef.entity;

import asksef.config.DateConverter;
import asksef.errors.UniqueCountryName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Entity
@Table(name = "COUNTRY", schema = "rest_app")
public class Country implements Serializable, Comparable<Country> {
    private static final Long serialLONGID = 1L;

    public Country() {
        this.lastUpdate = LocalDateTime.now();
    }

    public Country(String country) {
        this();

        this.country = country;
    }

    @Builder
    public Country(Long id, String country, LocalDateTime lastUpdate) {
        this.countryId = id;
        this.country = country;
        this.lastUpdate = lastUpdate;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUNTRY_ID", nullable = false)
    private Long countryId;


    @Getter
    @Column(name = "COUNTRY", length = 50, nullable = false, unique = true)
    @UniqueCountryName
    private String country;

    @Setter
    @OneToMany(targetEntity = City.class, mappedBy = "country", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<City> cityList;


    public List<City> getCityList() {
        return (cityList != null) ? cityList : new ArrayList<>();
    }

    public void addCity(City city) {
        List<City> cities = getCityList();
        if (!cities.contains(city)) {
            cities.add(city);
        }
        city.setCountry(this);
    }

    @Convert(converter = DateConverter.class)
    private LocalDateTime lastUpdate;

    @Basic(optional = false)
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Country country1 = (Country) o;
        return Objects.equals(country, country1.country);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(country);
    }

    @Override
    public String toString() {
        return "Country{" + "countryId=" + countryId + ", country= "
                + country + "}";
    }

    @Override
    public int compareTo(Country country) {
        return this.country.compareTo(country.getCountry());
    }
}