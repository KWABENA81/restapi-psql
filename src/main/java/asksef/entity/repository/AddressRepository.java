package asksef.entity.repository;



import asksef.entity.Address;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Transactional
public interface AddressRepository extends JpaRepository<Address, Long> {

    @Query("SELECT a from Address a WHERE a.gpsCode=(:code)")
    Collection<Address> findByCode(@Param("code") String code);

    @Query("SELECT a from Address a WHERE a.phone=(:phone)")
    Address findByPhone(@Param("phone") String phone);

}