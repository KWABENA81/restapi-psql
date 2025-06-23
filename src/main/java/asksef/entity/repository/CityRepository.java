package asksef.entity.repository;


import asksef.entity.City;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface CityRepository extends JpaRepository<City, Long> {

    @Query("SELECT c from City c WHERE c.city=(:city)")
    City findCityByName(@Param("city") String city);

}
