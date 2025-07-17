package com.asksef.entity.core;

import com.asksef.config.DateConverter;
import com.asksef.errors.UniqueCountryName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
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
    public Country(String country, LocalDateTime lastUpdate, Long id) {
        this.countryId = id;
        this.country = country;
        this.lastUpdate = lastUpdate;
    }

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "country_id_seq")
    @SequenceGenerator(name = "country_id_seq", sequenceName = "rest_app.country_country_id_seq", allocationSize = 1)
    @Column(name = "COUNTRY_ID", nullable = false)
    private Long countryId;

    @Getter
    @EqualsAndHashCode.Include
    @Column(name = "COUNTRY", length = 50, nullable = false, unique = true)
    @UniqueCountryName
    private String country;

    @Setter
    @ToString.Exclude
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
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        Country country1 = (Country) o;
//        return Objects.equals(country, country1.country);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(country);
//    }
//
//    @Override
//    public String toString() {
//        return "Country{" + "countryId=" + countryId + ", country= "
//                + country + "}";
//    }

    @Override
    public int compareTo(Country country) {
        return this.country.compareTo(country.getCountry());
    }
}