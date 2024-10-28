package com.example.demo.service;

import com.example.demo.config.SecurityConfig;
import com.example.demo.entity.Account;
import com.example.demo.entity.Customer;
import com.example.demo.entity.enums.Role;
import com.example.demo.exception.DuplicateEntity;
import com.example.demo.exception.EntityNotFoundException;
import com.example.demo.model.*;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.example.demo.repository.StylistRepository;
import javassist.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service // đánh dấu cho Spring Boot biết đây là lớp Service
public class AuthenticationService implements UserDetailsService {

    // sử lí logic, nghiệp vụ,...

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    TokenService tokenService;

    @Autowired
    EmailService emailService;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    StylistRepository stylistRepository;

    public AccountResponse register(RegisterRequest registerRequest) {
        Account account = modelMapper.map(registerRequest, Account.class);
        try {
            // new Date(): get current time
//            account.setCreateAt(new Date());
            String originalPassword = account.getPassword();
            account.setPassword(passwordEncoder.encode(originalPassword));

            // Initially, set image and sex to null since they will be updated later
            account.setImage(null);
            account.setSex(null);

            Account newAccount = accountRepository.save(account);

            // Create a new customer only if the role is CUSTOMER
            if (newAccount.getRole().equals(Role.CUSTOMER)) {
                Customer customer = new Customer();
                customer.setFullName(newAccount.getFullName());
                customer.setAccount(newAccount);
                customerRepository.save(customer);
            }
            //gửi mail về cho người dùng
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setReceiver(newAccount);
            emailDetail.setSubject("Chào mừng "+ newAccount.getFullName() +" đên với HairHarmony! ");
            emailDetail.setLink("https://www.google.com/");
            emailService.sendWelcomeEmail(emailDetail);

            return modelMapper.map(newAccount, AccountResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains(account.getPhone())) {
                throw new DuplicateEntity("Trùng số điện thoại!");
            } else if (e.getMessage().contains(account.getEmail())) {
                throw new DuplicateEntity("Trùng email!");
            }else{
                throw new DuplicateEntity("Kiểm tra lại Authentication Service!");
            }
        }
    }

    public AccountResponse login(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginRequest.getPhone(),
                    loginRequest.getPassword()
            ));

            // => tài khoản có tồn tại
            Account account = (Account) authentication.getPrincipal();
            AccountResponse accountResponse = modelMapper.map(account, AccountResponse.class);
            accountResponse.setToken(tokenService.generateToken(account));
            return accountResponse;
        }catch(Exception e) {
            throw new EntityNotFoundException("Tài khoản hoặc mật khẩu bị sai!");
        }
    }

//    public Account login(LoginRequest loginRequest) {
//        // xử lí logic login
//        // 2 trường hợp xảy ra
//        /*
//         *  1. Tồn tại
//         *  2. Không tồn tại
//         * */
//        boolean isValid = true;
//
//        if (isValid) {
//            // trả về thông tin của account
//        } else {
//            // error
//            throw new AccountNotFoundException("Account not found");
//        }
//        return null;
//    }
    public List<Account> getAllAccount(){
        List<Account> accounts = accountRepository.findAll();
        return accounts;
    }

    public Account login() {
        return null;
    }


    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        return accountRepository.findAccountByPhone(phone);
    }

    public Account getCurrentAccount(){
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return accountRepository.findAccountById(account.getId());
    }

    public void forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        Account account = accountRepository.findAccountByEmail(forgotPasswordRequest.getEmail());

        if(account==null){
            throw new EntityNotFoundException("Không tìm thấy tài khoản!");
        }else {
            EmailDetail emailDetail = new EmailDetail();
            emailDetail.setReceiver(account);
            emailDetail.setSubject("Xác nhận đặt lại mật khẩu");
            emailDetail.setLink("http://localhost:5137/reset-password?token=" + tokenService.generateToken(account));
            emailService.sendResetPasswordEmail(emailDetail);
        }
    }

    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        Account account = getCurrentAccount();
        account.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        accountRepository.save(account);
    }

//    private static final String CLIENT_ID = "YOUR_GOOGLE_CLIENT_ID";
//
//    public Optional<Account> loginWithGoogle(LoginGoogleRequest request) {
//        try {
//            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
//                    GoogleNetHttpTransport.newTrustedTransport(),
//                    JacksonFactory.getDefaultInstance()
//            ).setAudience(Collections.singletonList(CLIENT_ID)).build();
//
//            GoogleIdToken idToken = verifier.verify(request.getToken());
//            if (idToken != null) {
//                GoogleIdToken.Payload payload = idToken.getPayload();
//                String email = payload.getEmail();
//                String name = (String) payload.get("name");
//
//                Account account = accountRepository.findByEmail(email)
//                        .orElseGet(() -> {
//                            Account newAccount = new Account();
//                            newAccount.setEmail(email);
//                            newAccount.setFullName(name);
//                            newAccount.setRole(Role.CUSTOMER);
//                            return accountRepository.save(newAccount);
//                        });
//
//                return Optional.of(account);
//            } else {
//                return Optional.empty();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Optional.empty();
//        }
//    }
}
