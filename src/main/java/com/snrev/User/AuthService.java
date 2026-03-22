package com.snrev.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {
    LoginRepository loginRepository;

    @Autowired
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(LoginRepository repository, BCryptPasswordEncoder pEncoder)
    {
        this.loginRepository = repository;
        this.passwordEncoder = pEncoder;
    }

    public AuthResponse register(RegisterRequest request)
    {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = loginRepository.save(user);

        if(savedUser!=null)
        {
            return new AuthResponse("User registered successfully", savedUser.getEmail());
        }
        else {
            return new AuthResponse("User registration failed", null);
        }
    }

    public AuthResponse login(LoginRequest request)
    {
        Optional<User> user = loginRepository.findByEmail(request.getEmail());

        if(user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword()))
        {
            return new AuthResponse("Login Successfull", user.get().getEmail());
        }
        else
        {
            return  new AuthResponse("Login Failed", null);
        }
    }
}
