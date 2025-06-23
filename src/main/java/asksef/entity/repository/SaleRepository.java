package asksef.entity.repository;


import asksef.entity.Sale;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT s from Sale s WHERE s.saleNr=(:nr)")
    Sale findBySaleNr(@Param("nr") String nr);
}


