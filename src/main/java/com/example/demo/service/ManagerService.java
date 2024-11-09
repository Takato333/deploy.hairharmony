package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Manager;
import com.example.demo.entity.enums.Role;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.ManagerRequest;
import com.example.demo.model.ManagerResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.ManagerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ManagerService {
    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //C
    public ManagerResponse createManager(ManagerRequest managerRequest) {
        Manager manager = modelMapper.map(managerRequest, Manager.class);
        try {
            // Create a new account for the manager
            Account newAccount = createAccountForManager(managerRequest);

            // Set the newly created account to the manager
            manager.setAccount(newAccount);

            // Save the manager and its associated account
            Manager newManager = managerRepository.save(manager);

            // Map to ManagerResponse and include account details
            ManagerResponse response = modelMapper.map(newManager, ManagerResponse.class);
            response.setImage(newAccount.getImage());
            response.setSex(newAccount.getSex());

            return response;
        } catch (Exception e) {
            throw new DuplicateEntity("Bị trùng! Không thể tạo thêm quản lí!");
        }
    }

    private Account createAccountForManager(ManagerRequest managerRequest) {
        Account account = new Account();
        account.setEmail(managerRequest.getEmail());
        account.setPassword(passwordEncoder.encode(managerRequest.getPassword())); // Encoding password
        account.setPhone(managerRequest.getPhone());
        account.setFullName(managerRequest.getFullName());
        account.setRole(Role.MANAGER);
        account.setImage(managerRequest.getImage());
        account.setSex(managerRequest.getSex());

        // Save and return the created account
        return accountRepository.save(account);
    }

    //R
    public List<Manager> getAllManagers() {
        List<Manager> managerList = managerRepository.findManagersByIsDeletedFalse();
        return managerList;
    }

    public Manager getManagerById(long id) {
        Manager manager = managerRepository.findManagersById(id);
        if (manager == null) {
            throw new EntityNotFoundException("Không thấy quản lí!");
        }
        return manager;
    }

    //U
    public Manager updateManager(long id, ManagerRequest managerRequest) {
        Manager oldManager = getManagerById(id);
        oldManager.setFullName(managerRequest.getFullName());
        oldManager.setImage(managerRequest.getImage());
        oldManager.setSex(managerRequest.getSex());

        Account account = oldManager.getAccount();
        if (account != null) {
            account.setFullName(managerRequest.getFullName());
            account.setImage(managerRequest.getImage());
            account.setSex(managerRequest.getSex());
            account.setEmail(managerRequest.getEmail());
            account.setPhone(managerRequest.getPhone());
            account.setPassword(passwordEncoder.encode(managerRequest.getPassword()));
            accountRepository.save(account);
        }

        return managerRepository.save(oldManager);
    }


    //D
    public Manager deleteManager(long id) {
        Manager oldManager = getManagerById(id);
        oldManager.setDeleted(true);
        return managerRepository.save(oldManager);
    }
}
