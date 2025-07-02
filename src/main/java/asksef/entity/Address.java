package asksef.entity;

import asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Setter
@Entity
@Table(name = "ADDRESS", schema = "rest_app")
public class Address implements Serializable, Comparable<Address> {
    @Serial
    private static final long serialVersionUID = 1L;

    public Address() {
        this.lastUpdate = LocalDateTime.now();
    }

    @Builder
    public Address(Long id, String gpsCode, String phone, City city, LocalDateTime lastUpdate) {
        this.addressId = id;
        this.gpsCode = gpsCode;
        this.phone = phone;
        this.city = city;
        this.lastUpdate = lastUpdate;
    }


    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "address_id_seq")
    @SequenceGenerator(name = "address_id_seq", sequenceName = "rest_app.address_address_id_seq", allocationSize = 1)
    @Column(name = "ADDRESS_ID", nullable = false)
    private Long addressId;

    @Getter
    @Setter
    @Column(name = "GPS_CODE", length = 10, nullable = false)
    private String gpsCode;


    @Setter
    @Getter
    @Column(name = "PHONE", length = 20, nullable = false)
    private String phone;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CITY_ID", referencedColumnName = "CITY_ID")
    @JsonBackReference
    private City city;

    public void setCity(City city) {
        this.city = city;
        city.addAddress(this);
    }

    @Setter
    @OneToMany(targetEntity = Store.class, mappedBy = "address", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Store> storeList;

    public List<Store> getStoreList() {
        return (Objects.isNull(storeList)) ? new ArrayList<>() : storeList;
    }

    public void addStore(Store store) {
        List<Store> stores = getStoreList();
        if (!stores.contains(store)) {
            stores.add(store);
        }
        store.setAddress(this);
    }

    @Setter
    @OneToMany(targetEntity = Customer.class, mappedBy = "address", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Customer> customerList;

    public List<Customer> getCustomerList() {
        return (Objects.isNull(customerList))
                ? new ArrayList<>() : customerList;
    }

    public void addCustomer(Customer customer) {
        List<Customer> customers = getCustomerList();
        if (!customers.contains(customer)) {
            customers.add(customer);
        }
        customer.setAddress(this);
    }

//    @Setter
//    @OneToMany(targetEntity = Staff.class, mappedBy = "address", cascade = CascadeType.ALL)
//    @JsonManagedReference
//    @JsonIgnore
//    private List<Staff> staffList;
//
//    public List<Staff> getStaffList() {
//        return (Objects.isNull(staffList)) ? new ArrayList<>() : staffList;
//    }
//
//    public void addStaff(Staff staff) {
//        List<Staff> staffs = getStaffList();
//        if (!staffs.contains(staff)) {
//            staffs.add(staff);
//        }
//        staff.setAddress(this);
//    }

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
        Address address = (Address) o;
        return Objects.equals(gpsCode, address.gpsCode) && Objects.equals(phone, address.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gpsCode, phone);
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", gpsCode= " + gpsCode +
                ", phone= " + phone + "}";
    }

    @Override
    public int compareTo(Address address) {
        int value = this.gpsCode.compareTo(address.getGpsCode());
        if (value == 0) {
            value = this.phone.compareTo(address.getPhone());
        }
        return value;
    }
}
