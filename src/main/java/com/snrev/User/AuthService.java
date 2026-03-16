package com.snrev.User;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {
    LoginRepository loginRepository;

    public AuthService(LoginRepository repository)
    {
        this.loginRepository = repository;
    }

    public User register(RegisterRequest request)
    {
        	User user = new User();
            user.setEmail(request.getEmail());
            user.setName(request.getName());
            user.setPassword(request.getPassword());
            return loginRepository.save(user);
    }

    public User login(LoginRequest request)
    {
        //User user = loginRepository.findByEmail(request.getEmail()).orElse(null);

        Optional<User> user = loginRepository.findByEmail(request.getEmail());

        if(user.isPresent() && user.get().getPassword().equals(request.getPassword()))
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
