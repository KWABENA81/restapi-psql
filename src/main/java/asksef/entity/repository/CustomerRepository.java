package asksef.entity.repository;


import asksef.entity.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Transactional
public interface CustomerRepository extends JpaRepository<Customer, Long> {

//    @Query("SELECT c from Customer c WHERE c.lastName LIKE %:names% OR c.firstName LIKE %:names")
//    Collection<Customer> findLikeNames(@Param("names") String names);
}


