package asksef.entity;

import asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Setter
@Entity
@Table(name = "STORE", schema = "rest_app")
public class Store implements Serializable, Comparable<Store> {

    private static final Logger log = LoggerFactory.getLogger(Store.class);

    public Store() {
        this.lastUpdate = LocalDateTime.now();
    }

    public Store(String storeName) {
        this();
        this.storeName = storeName;
    }

    public Store(Long id, String storeName) {
        this();
        this.storeName = storeName;
        this.storeId = id;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "store_id_seq")
    @SequenceGenerator(name = "store_id_seq", sequenceName = "rest_app.store_store_id_seq", allocationSize = 1)
    @Column(name = "STORE_ID", nullable = false)
    private Long storeId;

    @Setter
    @Getter
    @Column(name = "STORE_NAME", length = 125, nullable = false)
    private String storeName;


    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STAFF_ID", referencedColumnName = "STAFF_ID")
    @JsonBackReference
    private Staff staff;

    public void setStaff(Staff staff) {
        this.staff = staff;
        staff.addStore(this);
    }

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ADDRESS_ID")
    @JsonBackReference
    private Address address;

    public void setAddress(Address address) {
        this.address = address;
        address.addStore(this);
    }

    @Setter
    @OneToMany(targetEntity = Inventory.class, mappedBy = "store", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Inventory> inventoryList;

    public List<Inventory> getInventoryList() {
        return (inventoryList != null) ? inventoryList : new ArrayList<>();
    }

    public void addInventory(Inventory inventory) {
        List<Inventory> inventories = getInventoryList();
        if (!inventories.contains(inventory)) {
            inventories.add(inventory);
        }
        inventory.setStore(this);
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
        Store store = (Store) o;
        return Objects.equals(storeName, store.storeName) && Objects.equals(address, store.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeName, address);
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", staff=" + staff +
                ", address=" + address +
                // ", inventoryList=" + inventoryList +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    public int compareTo(Store store) {
        int value = this.storeName.compareTo(store.getStoreName());
        if (value == 0) {
            value = this.address.compareTo(store.getAddress());
        }
        return value;
    }
}