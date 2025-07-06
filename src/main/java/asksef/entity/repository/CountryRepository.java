package asksef.entity.repository;


import asksef.entity.core.Country;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query("SELECT c from Country c WHERE c.country=(:country)")
    Optional<Country> findByName(@Param("country") String country);
}
