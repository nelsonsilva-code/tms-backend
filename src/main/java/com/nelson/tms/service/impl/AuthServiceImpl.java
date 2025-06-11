package com.nelson.tms.service.impl;

import com.nelson.tms.dto.JwtAuthResponse;
import com.nelson.tms.dto.LoginDto;
import com.nelson.tms.dto.CreateUserDto;
import com.nelson.tms.entity.Role;
import com.nelson.tms.entity.User;
import com.nelson.tms.exception.EmailAlreadyExistsException;
import com.nelson.tms.exception.InvalidPasswordException;
import com.nelson.tms.exception.UserNotFoundException;
import com.nelson.tms.exception.UsernameAlreadyExistsException;
import com.nelson.tms.repository.RoleRepository;
import com.nelson.tms.repository.UserRepository;
import com.nelson.tms.security.JwtTokenProvider;
import com.nelson.tms.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    public void createUser(CreateUserDto createUserDto, Authentication authentication) {

        if (userRepository.existsByUsername(createUserDto.getUsername())) {
           throw new UsernameAlreadyExistsException();
        }

        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            throw new EmailAlreadyExistsException();
        }

        User user = new User();

        user.setUsername(createUserDto.getUsername());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Role role = roleRepository.findByName(isAdmin ? createUserDto.getRole() : "ROLE_USER");

        user.setRole(role);

        userRepository.save(user);

    }

    @Override
    public JwtAuthResponse login(LoginDto loginDto) {

        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new UserNotFoundException());

        boolean passwordCorrect = passwordEncoder.matches(loginDto.getPassword(), user.getPassword());

        if (!passwordCorrect) {
            throw new InvalidPasswordException();
        }

        Authentication authentication =  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()
        ));


        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        String role = user.getRole().getName();

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(token);
        jwtAuthResponse.setRole(role);
        return jwtAuthResponse;
    }

    public HttpStatus delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        userRepository.flush();
        userRepository.delete(user);
        return HttpStatus.OK;
    }
}
