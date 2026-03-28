package com.snrev.User.Controller;

import com.snrev.User.DTO.RegisterResponse;
import com.snrev.User.DTO.LoginResponse;
import com.snrev.User.Service.AuthService;
import com.snrev.User.DTO.LoginRequest;
import com.snrev.User.DTO.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;



@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @GetMapping("/test")
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("test");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse obj =  authService.register(request);
        if(obj.getMessage() == "User registered successfully")
        {
            return ResponseEntity.ok(obj);
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse obj =  authService.login(request);
        if(obj.getMessage() == "Login Successfull")
        {
            return ResponseEntity.ok(obj);
        }
        else
        {
            System.out.println("Invalid credentials");
            return ResponseEntity.badRequest().build();
        }
    }
}