package com.nelson.tms.service.impl;

import com.nelson.tms.dto.JwtAuthResponse;
import com.nelson.tms.dto.LoginDto;
import com.nelson.tms.dto.CreateUserDto;
import com.nelson.tms.dto.UpdatePasswordDto;
import com.nelson.tms.entity.Role;
import com.nelson.tms.entity.User;
import com.nelson.tms.exception.InvalidPasswordException;
import com.nelson.tms.exception.UserNotFoundException;
import com.nelson.tms.exception.UsernameAlreadyExistsException;
import com.nelson.tms.repository.RoleRepository;
import com.nelson.tms.repository.UserRepository;
import com.nelson.tms.security.JwtTokenProvider;
import com.nelson.tms.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    private final Role defaultUserRole;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider) {
        this.userRepository        = userRepository;
        this.roleRepository        = roleRepository;
        this.passwordEncoder       = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider      = jwtTokenProvider;
        this.defaultUserRole = roleRepository
                .findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Default role ROLE_USER not found"));
    }

    public void createUser(CreateUserDto createUserDto) {
        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            throw new UsernameAlreadyExistsException();
        }

        User user = new User();
        user.setUsername(createUserDto.getUsername());
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        user.setRole(resolveRole(createUserDto.getRole()));

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

    public HttpStatus updatePassword(UpdatePasswordDto updatePasswordDto, Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    user.getUsername(),
                    updatePasswordDto.getOldPassword()
            ));
        } catch (BadCredentialsException ex) {
            return HttpStatus.UNAUTHORIZED;
        }

        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        userRepository.save(user);
        return HttpStatus.OK;
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    private Role resolveRole(String requestedRoleName) {
        if (!isAdmin()) {
            return defaultUserRole;
        }
        return Optional.ofNullable(requestedRoleName)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .flatMap(roleRepository::findByName)
                .orElse(defaultUserRole);
    }

    private boolean isAdmin() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
