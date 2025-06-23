package asksef.entity.service_impl;

import asksef.entity.Staff;
import asksef.entity.repository.StaffRepository;
import asksef.entity.service_interface.StaffServiceInterface;
import asksef.errors.CustomResourceExistsException;
import asksef.errors.CustomResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Service
public class StaffService implements StaffServiceInterface {

    private static final Logger log = LoggerFactory.getLogger(StaffService.class);
    private final StaffRepository staffRepository;

    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public Collection<Staff> findAll() {
        log.info("Find all staff");
        return this.staffRepository.findAll();
    }

    @Override
    public Collection<Staff> findAll(int pageNumber, int pageSize) {
        return this.staffRepository.findAll(PageRequest.of(pageNumber, pageSize)).getContent();
    }

    @Override
    public Staff findById(Long id) {
        Staff staff = staffRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Staff", "id", null, id)
        );
        log.info("Found staff with id: {}", id);
        return staff;
    }

    @Override
    public Staff save(Staff staff) {
        Optional<Staff> optional = this.staffRepository.findById(staff.getStaffId());
        if (optional.isPresent()) {
            throw new CustomResourceExistsException("Staff", "id", null, staff.getStaffId());
        }
        return staffRepository.save(staff);
    }

    @Override
    public Staff update(Staff staff) {
        Long id = staff.getStaffId();
        Optional<Staff> optional = staffRepository.findById(id);

        if (optional.isPresent()) {
            Staff staff1 = optional.get();
            staff1.setFirstName(staff.getFirstName());
            staff1.setLastName(staff.getLastName());
            staff1.setUsername(staff.getUsername());
            // staff1.setAddress(staff.getAddress());
            staff1.setStoreList(staff.getStoreList());
            staff1.setPaymentList(staff.getPaymentList());

            log.info("Updated staff: {}", staff);
            return staffRepository.save(staff1);
        } else {
            throw new CustomResourceNotFoundException("Staff", "id", null, id);
        }
    }

    public Staff update(Long id, Staff staff) {
        if (Objects.equals(id, staff.getStaffId())) {
            return update(staff);
        } else
            throw new CustomResourceNotFoundException("Staff", "id", null, id);
    }

    @Override
    public void delete(Staff staff) {
        delete(staff.getStaffId());
    }

    public void delete(Long id) {
        Staff staff1 = staffRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Staff", "id", null, id)
        );
        this.staffRepository.delete(staff1);
    }

    @Override
    public Long count() {
        return staffRepository.count();
    }

    public Staff findByUsername(String username) {
        return this.staffRepository.findByUsername(username);
    }
}
