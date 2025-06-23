package asksef.entity.repository;


import asksef.entity.Inventory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
//    @Modifying
//    @Query("SELECT * FROM Inventory WHERE inventory=(:country)")
//    Inventory updateInventory(Inventory inventory);
}
