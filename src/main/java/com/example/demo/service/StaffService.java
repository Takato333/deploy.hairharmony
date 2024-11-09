package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Staff;
import com.example.demo.entity.Stylist;
import com.example.demo.entity.enums.Role;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.StaffRequest;
import com.example.demo.model.StaffResponse;
import com.example.demo.model.StylistRequest;
import com.example.demo.model.StylistResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.StaffRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //C
    public StaffResponse createStaff(StaffRequest staffRequest) {
        Staff staff = modelMapper.map(staffRequest, Staff.class);
        try {
            // Create a new account for the staff
            Account newAccount = createAccountForStaff(staffRequest); // Refactored function for account creation

            // Set the newly created account to the staff
            staff.setAccount(newAccount);

            // Save the staff and its associated account
            Staff newStaff = staffRepository.save(staff);

            // Map to StaffResponse and include account details
            StaffResponse response = modelMapper.map(newStaff, StaffResponse.class);
            response.setImage(newAccount.getImage());
            response.setSex(newAccount.getSex());

            return response;
        } catch (Exception e) {
            throw new DuplicateEntity("Bị trùng! Không thể tạo thêm nhân viên!");
        }
    }

    private Account createAccountForStaff(StaffRequest staffRequest) {
        Account account = new Account();
        account.setEmail(staffRequest.getEmail());
        account.setPassword(passwordEncoder.encode(staffRequest.getPassword())); // Encoding password
        account.setPhone(staffRequest.getPhone());
        account.setFullName(staffRequest.getFullName());
        account.setRole(Role.STAFF); // Assuming staff always have this role
        account.setImage(staffRequest.getImage());
        account.setSex(staffRequest.getSex());

        // Save and return the created account
        return accountRepository.save(account);
    }

    //R
    public List<Staff> getAllStaff() {
        List<Staff> staffList = staffRepository.findStaffByIsDeletedFalse();
        return staffList;
    }

    //U
    public Staff updateStaff(long id, StaffRequest staff) {
        Staff oldStaff = staffRepository.findStaffById(id);

        if (oldStaff == null) throw new EntityNotFoundException("Không tìm thấy nhân viên!");

        oldStaff.setFullName(staff.getFullName());
        oldStaff.setImage(staff.getImage());
        oldStaff.setSex(staff.getSex());

        Account account = oldStaff.getAccount();
        if (account != null) {
            account.setFullName(staff.getFullName());
            account.setImage(staff.getImage());
            account.setSex(staff.getSex());
            account.setEmail(staff.getEmail());
            account.setPhone(staff.getPhone());
            String originalPassword = staff.getPassword();
            account.setPassword(passwordEncoder.encode(originalPassword));
            accountRepository.save(account);
        }
        return staffRepository.save(oldStaff);
    }

    //D
    public Staff deleteStaff(long id) {
        Staff oldStaff = getStaffById(id);
        oldStaff.setDeleted(true);
        return staffRepository.save(oldStaff);
    }

    public Staff getStaffById(long id) {
        Staff staff = staffRepository.findStaffById(id);
        if (staff == null) throw new EntityNotFoundException("Không tìm thấy nhân viên!");
        return staff;
    }


}
