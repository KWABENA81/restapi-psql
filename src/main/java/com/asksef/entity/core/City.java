package com.asksef.entity.core;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
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
@Table(name = "CITY", schema = "rest_app")
public class City implements Serializable, Comparable<City> {
    @Serial
    private static final long serialVersionUID = 1L;

    public City() {
        this.lastUpdate = LocalDateTime.now();
    }

    public City(String city, Country country) {
        this();
        this.city = city;
        this.country = country;
    }

    @Builder
    public City(String city, Country country, LocalDateTime lastUpdate, Long id) {
        this.cityId = id;
        this.city = city;
        this.country = country;
        this.lastUpdate = lastUpdate;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_id_seq")
    @SequenceGenerator(name = "city_id_seq", sequenceName = "rest_app.city_city_id_seq", allocationSize = 1)
    @Column(name = "CITY_ID", nullable = false)
    private Long cityId;

    @Getter
    @Setter
    @EqualsAndHashCode.Include
    @Column(name = "CITY", length = 50, nullable = false, unique = true)
    private String city;

    @Getter
    @EqualsAndHashCode.Include
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "COUNTRY_ID")
    @JsonBackReference
    private Country country;

    public void setCountry(Country country) {
        this.country = country;
        country.addCity(this);
    }

    @Setter
    @ToString.Exclude
    @OneToMany(targetEntity = Address.class, mappedBy = "city", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JsonIgnore
    private List<Address> addressList;

    public List<Address> getAddressList() {
        return (addressList != null) ? addressList : new ArrayList<>();
    }

    public void addAddress(Address address) {
        List<Address> addresses = getAddressList();
        if (!addresses.contains(address)) {
            addresses.add(address);
        }
        address.setCity(this);
    }

    @Getter
    @Basic//(optional = true)    //@CreationTimestamp
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastUpdate;
//
//    @Override
//    public boolean equals(Object o) {
//        if (o == null || getClass() != o.getClass()) return false;
//        City city1 = (City) o;
//        return Objects.equals(city, city1.city) && Objects.equals(country, city1.country);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(city, country);
//    }
//
//    @Override
//    public String toString() {
//        return "City{" +
//                "cityId=" + cityId +
//                ", city='" + city + '\'' +
//                ", country=" + country +
//                ", lastUpdate=" + lastUpdate +
//                '}';
//    }

    @Override
    public int compareTo(City city) {
        int value = this.city.compareTo(city.getCity());
        if (value == 0) {
            value = this.country.compareTo(city.getCountry());
        }
        return value;
    }
}
