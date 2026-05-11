package com.snrev.User.Service;

import java.util.Set;
import java.util.HashSet;
import com.snrev.User.DTO.RegisterResponse;
import com.snrev.User.DTO.LoginResponse;
import com.snrev.User.Entity.User;
import com.snrev.User.Entity.Role;
import com.snrev.User.DTO.LoginRequest;
import com.snrev.User.DTO.RegisterRequest;
import com.snrev.User.Repository.LoginRepository;
import com.snrev.User.Repository.RoleRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.snrev.User.Security.JwtUtil;

@Service
public class AuthService {
    private final LoginRepository loginRepository;
    private final RoleRepository roleRepository;
    private final CustomUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(LoginRepository repository,RoleRepository roleRepository ,BCryptPasswordEncoder pEncoder,
                       CustomUserDetailsService userDetailsService,
                       AuthenticationManager authenticationManager,
                       JwtUtil jwtUtil)
    {
        this.loginRepository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = pEncoder;
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    public RegisterResponse register(RegisterRequest request)
    {
        if(loginRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        
        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<String> sRoles = request.getRoles();
        Set<Role> userRoles = new HashSet<>();
        for(String element :sRoles)
        {
            //covert element to enum
            Role.ERole eRole = Role.ERole.valueOf(element);
            Role role = roleRepository.findByName(eRole).get();
            userRoles.add(role);
        }

        user.setRoles(userRoles);
        User savedUser = loginRepository.save(user);

        if(savedUser!=null)
        {
            return new RegisterResponse("User registered successfully", savedUser.getEmail());
        }
        else {
            throw new RuntimeException("User registration failed");
        }
    }

    public LoginResponse login(LoginRequest request)
    {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }
        // At this point we already know user exists therfore no check required
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        String token = jwtUtil.generateToken(userDetails);
        return new LoginResponse("Login Successfull", token);
    }
}
