package com.nelson.tms.service.impl;

import com.nelson.tms.dto.LoginDto;
import com.nelson.tms.dto.RegisterDto;
import com.nelson.tms.entity.Role;
import com.nelson.tms.entity.User;
import com.nelson.tms.exception.EmailAlreadyExistsException;
import com.nelson.tms.exception.InvalidPasswordException;
import com.nelson.tms.exception.UserNotFoundException;
import com.nelson.tms.exception.UsernameAlreadyExistsException;
import com.nelson.tms.repository.RoleRepository;
import com.nelson.tms.repository.UserRepository;
import com.nelson.tms.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;

    public void register(RegisterDto registerDto) {

        if (userRepository.existsByUsername(registerDto.getUsername())) {
           throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User();

        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByName("ROLE_USER");

        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user);

    }

    @Override
    public HttpStatus login(LoginDto loginDto) {

        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException());

        System.out.println("Checking password");
        boolean passwordCorrect = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
        if (!passwordCorrect) {
            System.out.println("Wrong password");
            throw new InvalidPasswordException();
        }

        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return HttpStatus.OK;
    }
}
