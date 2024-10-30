package com.example.demo.api;

import com.example.demo.entity.Account;
import com.example.demo.model.*;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.TokenService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "api")
//@PreAuthorize("hasAuthority('MANAGER')") HÀM DÙNG ĐỂ PHÂN QUYỀN
public class AuthenticationAPI {

    //DI: Dependence Injection
    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    TokenService tokenService;

    @PostMapping("register")
    public ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {
        //nhờ authenticationService => tạo dùm account
        AccountResponse newAccount = authenticationService.register(registerRequest);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest) {
        //nhờ authenticationService => tạo dùm account
        AccountResponse newAccount = authenticationService.login(loginRequest);
        return ResponseEntity.ok(newAccount);
    }

    @GetMapping("account")
    public ResponseEntity getAllAccount() {
        List<Account> accounts = authenticationService.getAllAccount();
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("forgot-password")
    public ResponseEntity forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest ) {
        authenticationService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok("Forgot Password successfully");
    }

    @PostMapping("reset-password")
    public ResponseEntity resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest ) {
        authenticationService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok("Reset Password successfully");
    }

    private static final String GOOGLE_CLIENT_ID = "41329500178-l75j7k4054582dko4m02ahsaig05hn4k.apps.googleusercontent.com";

    @PostMapping("/login-google")
    public ResponseEntity<String> checkLoginGoogle(@RequestBody LoginGoogleRequest request) {
        try {
            // Create a verifier
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID)) // Specify the client ID
                    .build();

            // Verify the token
            GoogleIdToken idToken = verifier.verify(request.getToken());
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                // Get the user information
                String userId = payload.getSubject(); // Use this ID to identify the user
                String email = payload.getEmail(); // Get the user's email

                // Here, you should find or create the account in your database
                Account account = authenticationService.findOrCreateAccount(email); // Implement this method based on your logic

                // Generate JWT token
                String token = tokenService.generateToken(account);

                return ResponseEntity.ok(token);
            } else {
                return ResponseEntity.status(401).body("Invalid Google token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid Google token");
        }
    }

}
