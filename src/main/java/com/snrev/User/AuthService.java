package com.snrev.User;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    LoginRepository loginRepository;
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
        User user = loginRepository.findByEmail(request.getId()).orElse(null);
        if(user != null && user.getPassword().equals(request.getPassword()))
        {
            return user;
        }
        else
        {
            return null;
        }
    }
}
