package asksef.entity.service_impl;

import asksef.entity.core.Staff;
import asksef.entity.model.StaffModel;
import asksef.entity.repository.StaffRepository;
import asksef.entity.service_interface.StaffServiceInterface;
import asksef.errors.CustomResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class StaffService implements StaffServiceInterface {

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

    @Transactional
    @Override
    public Staff save(Staff staff) {
        return staffRepository.save(staff);
    }

    @Transactional
    public Staff save(@Valid StaffModel staffModel) {
        Staff staff = Staff.builder()
                .staffId(staffModel.getStaffId())
                .firstName(staffModel.getFirstName())
                .lastName(staffModel.getLastName())
                .username(staffModel.getUsername())
                .lastUpdate(LocalDateTime.now())
                .build();
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
        return this.staffRepository.findByUsername(username).orElseThrow(
                () -> new CustomResourceNotFoundException("staff", "username", null, username)
        );
    }

//    public Address findAddressOfCustomer(Long id) {
//        return this.staffRepository.findAddressOfCustomer(id).orElseThrow(
//                () -> new CustomResourceNotFoundException("staff", "id", null, id)
//        );
//    }
}
