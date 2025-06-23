package asksef.entity.repository;


import asksef.entity.Item;
import asksef.entity.Store;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Transactional
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query("SELECT s from Store s WHERE s.storeName LIKE %:name%")
    Collection<Store> findByStoreName(@Param("name") String name);

//@Query("SELECT x from Item x WHERE x.itemDesc LIKE %:desc%")
//Collection<Item> findByDescLike(@Param("desc") String desc);
}
