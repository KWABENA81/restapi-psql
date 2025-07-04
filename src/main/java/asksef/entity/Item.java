package asksef.entity;

import asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Setter
@Entity
@Table(name = "ITEM", schema = "rest_app")
public class Item implements Serializable, Comparable<Item> {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(Item.class);

    public Item() {
        this.lastUpdate = LocalDateTime.now();
        this.saleInfo = String.valueOf(Math.abs(LocalDateTime.now().hashCode()));
    }

    public Item(String saleInfo) {
        this.saleInfo = saleInfo;
    }

    //  for testing
    public Item(Long id) {
        this();
        this.itemId = id;
    }

    @Builder
    public Item(Long id, String saleInfo, String itemName, String itemCode, String itemDesc, Float itemCost) {
        this.itemId = id;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.itemCost = itemCost;
        this.saleInfo = saleInfo;
        this.itemDesc = itemDesc;
    }

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_id_seq")
    @SequenceGenerator(name = "item_id_seq", sequenceName = "rest_app.item_item_id_seq", allocationSize = 1)
    @Column(name = "ITEM_ID", nullable = false)
    private Long itemId;

    @Getter
    @Setter
    @Column(name = "ITEM_NAME", nullable = false, length = 128)
    private String itemName;

    @Getter
    @Setter
    @Column(name = "ITEM_CODE", nullable = false, length = 128)
    private String itemCode;

    @Getter
    @Setter
    @Column(name = "ITEM_DESC", length = 128)
    private String itemDesc;

    @Getter
    @Setter
    @Column(name = "ITEM_COST", nullable = false)
    private Float itemCost;

    @Getter
    @Setter
    @Column(name = "SALE_INFO", nullable = false)
    private String saleInfo;

    @Convert(converter = DateConverter.class)
    private LocalDateTime lastUpdate;

    @Basic(optional = false)
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    @Setter
    @OneToMany(targetEntity = Inventory.class, mappedBy = "item", cascade = CascadeType.ALL)
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
        inventory.setItem(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(itemName, item.itemName) && Objects.equals(itemDesc, item.itemDesc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, itemDesc);
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", itemName='" + itemName + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", itemDesc='" + itemDesc + '\'' +
                ", itemCost=" + itemCost +
                ", saleInfo='" + saleInfo + '\'' +
                ", lastUpdate=" + lastUpdate +
                //     ", inventoryList=" + inventoryList +
                '}';
    }

    @Override
    public int compareTo(Item item) {
        int value = this.itemName.compareTo(item.getItemName());
        if (value == 0) {
            value = this.saleInfo.compareTo(item.getSaleInfo());
        }
        return value;
    }
}