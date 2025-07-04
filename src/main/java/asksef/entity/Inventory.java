package asksef.entity;

import asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Setter
@Entity
@Table(name = "INVENTORY", schema = "rest_app")
public class Inventory implements Serializable, Comparable<Inventory> {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(Inventory.class);

    public Inventory() {
        this.lastUpdate = LocalDateTime.now();
    }

    @Builder
    public Inventory(Long inventoryId, Item item, Store store, Integer stockQty, Integer reorderQty, LocalDateTime lastUpdate) {
        this.inventoryId = inventoryId;
        this.item = item;
        this.store = store;
        this.stockQty = stockQty;
        this.reorderQty = reorderQty;
        this.lastUpdate = lastUpdate;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_id_seq")
    @SequenceGenerator(name = "inventory_id_seq", sequenceName = "rest_app.inventory_inventory_id_seq", allocationSize = 1)
    @Column(name = "INVENTORY_ID", nullable = false)
    private Long inventoryId;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID", referencedColumnName = "ITEM_ID")
    @JsonBackReference
    @JsonIgnore
    private Item item;

    public void setItem(Item item) {
        this.item = item;
        item.addInventory(this);
    }

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID", referencedColumnName = "STORE_ID")
    @JsonBackReference
    @JsonIgnore
    private Store store;

    public void setStore(Store store) {
        this.store = store;
        store.addInventory(this);
    }

    @Getter
    @Column(name = "STOCK_QTY")
    private Integer stockQty;

    public void setStockQty(int stockQty) {
        this.stockQty = stockQty;
    }

    @Getter
    @Column(name = "REORDER_QTY")
    private Integer reorderQty;

    public void setReorderQty(int reorderQty) {
        this.reorderQty = reorderQty;
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
        Inventory inventory = (Inventory) o;
        return Objects.equals(item, inventory.item) && Objects.equals(store, inventory.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, store);
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "inventoryId=" + inventoryId +
                ", item=" + item +
                ", store=" + store +
                ", stockQty=" + stockQty +
                ", reorderQty=" + reorderQty +
                ", lastUpdate=" + lastUpdate +
                '}';
    }

    @Override
    public int compareTo(Inventory inventory) {
        int value = this.item.compareTo(inventory.getItem());
        if (value == 0) {
            value = this.store.compareTo(inventory.getStore());
        }
        return value;
    }
}