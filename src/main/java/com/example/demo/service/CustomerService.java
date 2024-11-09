package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.CustomerRequest;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    //Create
    public Customer createCustomer(CustomerRequest customerRequest) {
    //add customer vào database bằng repository
        try {
            Customer customer = modelMapper.map(customerRequest, Customer.class);

            //Xác định account nào tạo cái customer này
            //thông qua đc cái filter r
            //lưu lại đc cái account yêu cầu tạo customer này r
            Account accountRequest = authenticationService.getCurrentAccount();
            customer.setAccount(accountRequest);

            //save account
            accountRepository.save(accountRequest);

            Customer newCustomer = customerRepository.save(customer);
            return newCustomer;//trả về 1 thằng mới
        }catch (Exception e){
            throw new DuplicateEntity("Bị trùng! Không thể tạo thêm khách hàng!");
        }
    }

    //Read
    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findCustomerByIsDeletedFalse();
        return customers;
    }

    //Update
    public Customer update(long id, CustomerRequest customer) {
        //bước 1: tìm ra customer cần đc update
        Customer oldCustomer = customerRepository.findCustomerById(id);

        if(oldCustomer == null) throw new EntityNotFoundException("Không tìm thấy khách hàng!");
        // => có tồn tại

        //bước 2: cập nhật thông tin của nó
        oldCustomer.setFullName(customer.getFullName());
        oldCustomer.setSex(customer.getSex());
        oldCustomer.setImage(customer.getImage());

        // Sync changes back to Account
        Account account = oldCustomer.getAccount();
        if (account != null) {
            account.setFullName(customer.getFullName());
            account.setSex(customer.getSex());
            account.setImage(customer.getImage());
            account.setEmail(customer.getEmail());
            account.setPhone(customer.getPhone());
            String originalPassword = customer.getPassword();
            account.setPassword(passwordEncoder.encode(originalPassword));
            accountRepository.save(account);
        }

        //bước 3: lưu xuống database
        return customerRepository.save(oldCustomer);
    }

    //Delete
    public Customer deleteCustomer(long id) {
        Customer oldCustomer = getCustomerById(id);
        oldCustomer.setDeleted(true);
        return customerRepository.save(oldCustomer);
    }

    public Customer getCustomerById(long id) {
        Customer oldCustomer = customerRepository.findCustomerById(id);
        if(oldCustomer == null) throw new EntityNotFoundException("Không tìm thấy khách hàng!");
        //if user.status == "BLOCK" ==> throw new EntityNotFoundException("Customer not found");
        return oldCustomer;
    }
}
