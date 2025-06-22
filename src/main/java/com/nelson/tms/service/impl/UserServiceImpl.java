package com.nelson.tms.service.impl;

import com.nelson.tms.dto.*;
import com.nelson.tms.entity.Role;
import com.nelson.tms.entity.User;
import com.nelson.tms.exception.UserNotFoundException;
import com.nelson.tms.exception.UsernameAlreadyExistsException;
import com.nelson.tms.repository.RoleRepository;
import com.nelson.tms.repository.UserRepository;
import com.nelson.tms.service.UserService;
import com.nelson.tms.utils.RandomPasswordGenerator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private RandomPasswordGenerator randomPasswordGenerator;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final Role defaultUserRole;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           RandomPasswordGenerator randomPasswordGenerator) {
        this.userRepository        = userRepository;
        this.roleRepository        = roleRepository;
        this.passwordEncoder       = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.randomPasswordGenerator = randomPasswordGenerator;
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
        String randomPassword = randomPasswordGenerator.generateRandomPassword(18);
        logger.info("Created "+ user.getUsername() + " with password - " + randomPassword); // -> Logging the password because there is currently no way of sending it to the user (e.g. email service)
        user.setPassword(passwordEncoder.encode(randomPassword));
        user.setRole(resolveRole(createUserDto.getRole()));

        userRepository.save(user);
    }

    public HttpStatus delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        userRepository.flush();
        userRepository.delete(user);
        return HttpStatus.OK;
    }

    public HttpStatus updatePassword(UpdatePasswordDto updatePasswordDto, Long targetUserId) {
        User caller = getCallingUser();

        if (!caller.getId().equals(targetUserId)) {
            logger.error("❌ Unauthorized attempt: user ID "
                    + caller.getId() + " tried to change password of user ID " + targetUserId);
            return HttpStatus.FORBIDDEN;
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            caller.getUsername(),
                            updatePasswordDto.getCurrentPassword()
                    )
            );
        } catch (BadCredentialsException ex) {
            logger.error("❌ User " + caller.getUsername() + " provided a wrong current password");
            return HttpStatus.UNAUTHORIZED;
        }

        caller.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        userRepository.save(caller);
        logger.info("✅ User " + caller.getUsername()
                + " (ID " + caller.getId() + ") successfully updated their password");
        return HttpStatus.OK;
    }

    public HttpStatus updateRole(String newRole, Long userId) {
        logger.info("1");

        System.out.println(newRole);
        Role role = roleRepository.findByName(newRole).orElseThrow(() -> new IllegalStateException("Given role not found"));
        logger.info("2");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException());
        logger.info("3");

        user.setRole(role);
        logger.info("4");
        System.out.println(user);
        userRepository.save(user);
        return HttpStatus.OK;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getCurrentUser() {
        return getCallingUser();
    }
    public User getUser(Long id) {
        User caller = getCallingUser();

        User target = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        if (!caller.getId().equals(target.getId())) {
            log.error("Unauthorized access: user ID {} tried to fetch user ID {}",
                    caller.getId(), target.getId());
            throw new AccessDeniedException("You may only view your own data");
        }

        return target;
    }


    private User getCallingUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String callerUsername = auth.getName();

        return userRepository.findByUsername(callerUsername)
                .orElseThrow(() -> new UserNotFoundException());
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
