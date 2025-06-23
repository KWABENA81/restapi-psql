package asksef.entity.repository;


import asksef.entity.Staff;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Transactional
public interface StaffRepository extends JpaRepository<Staff, Long> {

    @Query("SELECT s from Staff s WHERE s.firstName=(:names) OR s.lastName=(:names)")
    Collection<Staff> findByNames(@Param("names") String names);

    @Query("SELECT s from Staff s WHERE s.username=(:username)")
    Staff findByUsername(@Param("username") String username);

}
