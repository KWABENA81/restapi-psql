package com.asksef.entity.core;

import com.asksef.config.DateConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Setter
@Entity
@Table(name = "ITEM", schema = "rest_app")
public class Item implements Serializable, Comparable<Item> {
    @Serial
    private static final long serialVersionUID = 1L;

    public Item() {
        this.lastUpdate = LocalDateTime.now();
    }

    //  for testing
    public Item(Long id) {
        this();
        this.itemId = id;
    }

    @Builder
    public Item(Long itemId, String itemName, String itemCode, String itemDesc, Float itemCost, LocalDateTime lastUpdate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemCode = itemCode;
        this.itemDesc = itemDesc;
        this.itemCost = itemCost;
        this.lastUpdate = lastUpdate;
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

    @Convert(converter = DateConverter.class)
    private LocalDateTime lastUpdate;

    @Basic(optional = false)
    @Column(name = "LAST_UPDATE")
    @Temporal(TemporalType.TIMESTAMP)
    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    @Setter
    @OneToMany(targetEntity = Order.class, mappedBy = "item", cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private List<Order> orderList;

    public List<Order> getOrderList() {
        return (orderList != null) ? orderList : new ArrayList<>();
    }

    public void addOrder(Order order) {
        List<Order> orders = getOrderList();
        if (!orders.contains(order))
            orders.add(order);
        order.setItem(this);
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
                ", lastUpdate=" + lastUpdate +
                //     ", inventoryList=" + inventoryList +
                '}';
    }

    @Override
    public int compareTo(Item item) {
        int value = this.itemCode.compareTo(item.getItemCode());
        if (value == 0) {
            value = this.itemName.compareTo(item.getItemName());
        }
        return value;
    }
}