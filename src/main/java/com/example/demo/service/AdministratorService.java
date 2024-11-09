package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Administrator;
import com.example.demo.entity.enums.Role;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.AdministratorRequest;
import com.example.demo.model.AdministratorResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.AdministratorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdministratorService {
    @Autowired
    AdministratorRepository administratorRepository;

    @Lazy
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //C
    public AdministratorResponse createAdministrator (AdministratorRequest administratorRequest) {
        Administrator administrator = modelMapper.map(administratorRequest, Administrator.class);
        try {
            // Create a new account for the administrator
            Account newAccount = createAccountForAdministrator(administratorRequest);

            // Set the newly created account to the administrator
            administrator.setAccount(newAccount);

            // Save the administrator and its associated account
            Administrator newAdministrator = administratorRepository.save(administrator);

            // Map to AdministratorResponse and include account details
            AdministratorResponse response = modelMapper.map(newAdministrator, AdministratorResponse.class);
            response.setImage(newAccount.getImage());
            response.setSex(newAccount.getSex());

            return response;
        } catch (Exception e) {
            throw new DuplicateEntity("Bị trùng! Không tìm thấy admin!");
        }

    }

    private Account createAccountForAdministrator(AdministratorRequest administratorRequest) {
        Account account = new Account();
        account.setEmail(administratorRequest.getEmail());
        account.setPassword(passwordEncoder.encode(administratorRequest.getPassword())); // Encoding password
        account.setPhone(administratorRequest.getPhone());
        account.setFullName(administratorRequest.getFullName());
        account.setRole(Role.ADMINISTRATOR);
        account.setImage(administratorRequest.getImage());
        account.setSex(administratorRequest.getSex());

        // Save and return the created account
        return accountRepository.save(account);
    }

    //R
    public List<Administrator> getAllAdministrators() {
        List<Administrator> administratorList = administratorRepository.findAdministratorByIsDeletedFalse();
        return administratorList;
    }

    public Administrator getAdministratorById(long id) {
        Administrator administrator = administratorRepository.findAdministratorById(id);
        if (administrator == null) throw new EntityNotFoundException("Không tìm thấy admin!");
        return administrator;
    }

    //U
    public Administrator updateAdministrator(long id, AdministratorRequest administratorRequest) {
        Administrator oldAdministrator = getAdministratorById(id);
        oldAdministrator.setFullName(administratorRequest.getFullName());
        oldAdministrator.setImage(administratorRequest.getImage());
        oldAdministrator.setSex(administratorRequest.getSex());

        Account account = oldAdministrator.getAccount();
        if (account != null) {
            account.setFullName(administratorRequest.getFullName());
            account.setImage(administratorRequest.getImage());
            account.setSex(administratorRequest.getSex());
            account.setEmail(administratorRequest.getEmail());
            account.setPhone(administratorRequest.getPhone());
            account.setPassword(passwordEncoder.encode(administratorRequest.getPassword()));
            accountRepository.save(account);
        }
        return administratorRepository.save(oldAdministrator);
    }

    //D
    public Administrator deleteAdministrator(long id) {
        Administrator oldAdministrator = getAdministratorById(id);
        oldAdministrator.setDeleted(true);
        return administratorRepository.save(oldAdministrator);
    }
}
