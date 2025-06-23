package asksef.entity.service_interface;


import asksef.entity.Staff;

import java.util.Collection;

public interface StaffServiceInterface {
    Collection<Staff> findAll();

    Collection<Staff> findAll(int pageNumber, int pageSize);

    Staff findById(Long id);

    Staff save(Staff staff);

    Staff update(Staff staff);

    void delete(Staff staff);

    Long count();
}
