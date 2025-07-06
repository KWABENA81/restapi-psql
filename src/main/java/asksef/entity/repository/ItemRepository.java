package asksef.entity.repository;

import asksef.entity.core.Item;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Transactional
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT x from Item x WHERE x.itemCode=(:code)")
    Item findByCode(@Param("code") String code);

    @Query("SELECT x from Item x WHERE x.itemDesc LIKE %:desc%")
    Collection<Item> findByDescLike(@Param("desc") String desc);

    @Query("SELECT x from Item x WHERE x.itemName LIKE %:name%")
    Collection<Item> findByNameLike(@Param("name") String name);
}

