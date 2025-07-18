package com.asksef.entity.service_impl;

import com.asksef.entity.core.Order;
import com.asksef.entity.core.Payment;
import com.asksef.entity.core.Staff;
import com.asksef.entity.core.Store;
import com.asksef.entity.model.StaffModel;
import com.asksef.entity.repository.StaffRepository;
import com.asksef.entity.service_interface.StaffServiceInterface;
import com.asksef.errors.CustomResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
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

    public Page<Staff> findAll(Pageable pageable) {
        return this.staffRepository.findAll(pageable);
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

    public List<Payment> findStaffPayments(Long id) {
        Staff staff = this.staffRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Country", "id", null, id)
        );
        return staff.getPaymentList();
    }

    public List<Order> findStaffOrders(Long id) {
        Staff staff = this.staffRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Country", "id", null, id)
        );
        return staff.getOrderList();
    }

    public List<Store> findStaffStores(Long id) {
        Staff staff = this.staffRepository.findById(id).orElseThrow(
                () -> new CustomResourceNotFoundException("Country", "id", null, id)
        );
        return staff.getStoreList();
    }

}
