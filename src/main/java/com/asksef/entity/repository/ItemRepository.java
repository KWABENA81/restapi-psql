package com.asksef.entity.repository;

import com.asksef.entity.core.Item;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Item, Long>, PagingAndSortingRepository<Item, Long> {

    @Query("SELECT x from Item x WHERE x.itemCode=(:code)")
    Optional<Item> findByCode(@Param("code") String code);

    @Query("SELECT x from Item x WHERE x.itemDesc LIKE %:desc%")
    Collection<Item> findByDescLike(@Param("desc") String desc);

    @Query("SELECT x from Item x WHERE x.itemName LIKE %:name%")
    Collection<Item> findByNameLike(@Param("name") String name);
}

