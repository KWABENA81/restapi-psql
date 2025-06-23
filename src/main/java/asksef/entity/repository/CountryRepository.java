package asksef.entity.repository;


import asksef.entity.Country;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Transactional
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query("SELECT c from Country c WHERE c.country=(:country)")
    Country findCountryByName(@Param("country") String country);

    @Query("SELECT c from Country c WHERE c.country LIKE %:country%")
    Collection<Country> findLikeName(@Param("country") String country);
}
