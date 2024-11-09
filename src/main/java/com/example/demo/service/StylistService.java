package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Stylist;
import com.example.demo.entity.enums.Role;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.StylistRequest;
import com.example.demo.model.StylistResponse;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.StylistRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StylistService {

    @Autowired
    StylistRepository stylistRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //C
    public StylistResponse createStylist(StylistRequest stylistRequest) {
        Stylist stylist = modelMapper.map(stylistRequest, Stylist.class);
        try {
            // Create a new account for the stylist
            Account newAccount = createAccountForStylist(stylistRequest); // Refactored function for account creation

            // Set the newly created account to the stylist
            stylist.setAccount(newAccount);

            // Save the stylist and its associated account
            Stylist newStylist = stylistRepository.save(stylist);

            // Map to StylistResponse and include account details
            StylistResponse response = modelMapper.map(newStylist, StylistResponse.class);
            response.setImage(newAccount.getImage());
            response.setSex(newAccount.getSex());

            return response;
        } catch (Exception e) {
            throw new DuplicateEntity("Bị trùng! Không thể tạo thợ!");
        }
    }


        private Account createAccountForStylist(StylistRequest stylistRequest) {
            Account account = new Account();
            account.setEmail(stylistRequest.getEmail());
            account.setPassword(passwordEncoder.encode(stylistRequest.getPassword())); // Encoding password
            account.setPhone(stylistRequest.getPhone());
            account.setFullName(stylistRequest.getFullName());
            account.setRole(Role.STYLIST); // Assuming stylists always have this role
            account.setImage(stylistRequest.getImage());
            account.setSex(stylistRequest.getSex());

            // Save and return the created account
            return accountRepository.save(account);
        }

        //R
    public List<Stylist> getAllStylists() {
        List<Stylist> stylists = stylistRepository.findStylistsByIsDeletedFalse();
        return stylists;
    }

    //U
    public Stylist updateStylist(long id, StylistRequest stylist) {
        Stylist oldStylist =  stylistRepository.findStylistByIdAndIsDeletedFalse(id);

        if(oldStylist == null) throw new EntityNotFoundException("Không tìm thấy thợ!");

        oldStylist.setFullName(stylist.getFullName());
        oldStylist.setDescription(stylist.getDescription());
        oldStylist.setImage(stylist.getImage());
        oldStylist.setSex(stylist.getSex());

        Account account = oldStylist.getAccount();
        if(account != null){
            account.setFullName(stylist.getFullName());
            account.setImage(stylist.getImage());
            account.setSex(stylist.getSex());
            account.setEmail(stylist.getEmail());
            account.setPhone(stylist.getPhone());
            String originalPassword = stylist.getPassword();
            account.setPassword(passwordEncoder.encode(originalPassword));
            accountRepository.save(account);
        }
            return stylistRepository.save(oldStylist);
    }

    //D
    public Stylist deleteStylist(long id) {
        Stylist oldStylist = getStylistById(id);
        oldStylist.setDeleted(true);
        return stylistRepository.save(oldStylist);
    }

    public Stylist getStylistById(long id) {
        Stylist stylist = stylistRepository.findStylistByIdAndIsDeletedFalse(id);
        if(stylist == null) throw new EntityNotFoundException("Không tìm thấy thợ!");
        return stylist;
    }

    public List<Stylist> getStylistsByLevel(int level){
        List<Stylist> stylists = stylistRepository.findStylistsByLevel(level);
        if(stylists == null) throw new EntityNotFoundException("Không tìm thấy thợ ở cấp độ này!");
        return stylists;
    }
}
