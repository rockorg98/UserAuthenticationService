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

    public User register(RegisterRequest request)
    {
        	User user = new User();
            user.setEmail(request.getEmail());
            user.setName(request.getName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            return loginRepository.save(user);
    }

    public User login(LoginRequest request)
    {
        Optional<User> user = loginRepository.findByEmail(request.getEmail());

        if(user.isPresent() && passwordEncoder.matches(request.getPassword(), user.get().getPassword()))
        {
            return user.get();
        }
        else
        {
            System.out.println("Can not find by email");
            return null;
        }
    }
}
