package com.snrev.User;

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
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User obj =  authService.register(request);
        if(obj != null)
        {
            return ResponseEntity.ok(obj);
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User obj =  authService.login(request);
        if(obj != null)
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