package com.asksef.entity.repository;


import com.asksef.entity.core.Inventory;
import com.asksef.entity.core.Item;
import com.asksef.entity.core.Store;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface InventoryRepository extends JpaRepository<Inventory, Long> {


    @Query(
            nativeQuery = true,
            value = "SELECT s.store_id, s.store_name, s.last_update, s.staff_id, s.address_id, v.inventory_id " +
                    "FROM rest_app.Store s  " +
                    "INNER JOIN rest_app.Inventory v " +
                    "ON s.store_id = v.store_id " +
                    "WHERE v.inventory_id=:id")
    Optional<Store> findStoreOfInventory(@Param("id") Long id);

    @Query(
            nativeQuery = true,
            value = "SELECT i.item_id, i.item_name, i.item_code, i.itemdesc, i.item_cost, i.sale_info, i.last_update, v.inventory_id " +
                    "FROM rest_app.Item i  " +
                    "INNER JOIN rest_app.Inventory v " +
                    "ON i.item_id = v.item_id " +
                    "WHERE v.inventory_id=:id")
    Optional<Item> findItemOfInventory(@Param("id") Long id);
}
