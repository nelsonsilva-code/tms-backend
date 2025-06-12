package com.nelson.tms.service.impl;

import com.nelson.tms.dto.*;
import com.nelson.tms.entity.Permission;
import com.nelson.tms.entity.Role;
import com.nelson.tms.entity.User;
import com.nelson.tms.exception.InvalidPasswordException;
import com.nelson.tms.exception.RoleAlreadyExistsException;
import com.nelson.tms.exception.UserNotFoundException;
import com.nelson.tms.exception.UsernameAlreadyExistsException;
import com.nelson.tms.repository.RoleRepository;
import com.nelson.tms.repository.UserRepository;
import com.nelson.tms.security.JwtTokenProvider;
import com.nelson.tms.service.AuthenticationService;
import com.nelson.tms.service.RoleService;
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
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public HttpStatus delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException());

        userRepository.flush();
        userRepository.delete(user);
        return HttpStatus.OK;
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public void createRole(RoleDto roleDto) {
        String givenRoleName = roleDto.getName();

        String roleName = givenRoleName.startsWith("ROLE_")
                ? givenRoleName
                : "ROLE_" + givenRoleName;

        if(roleRepository.findByName(roleName).isPresent()) {
            throw new RoleAlreadyExistsException();
        }

        Set<Permission> perms = roleDto.getPermissions().stream()
                .collect(Collectors.toSet());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));

        if (perms.remove(Permission.DELETE) && !isAdmin) {
            System.out.println("Non-admin user attempted to assign DELETE; stripped out - " +  auth.getName());
        }

        Role role = new Role();
        role.setName(roleName);
        role.setPermissions(perms);

        roleRepository.save(role);
    }
}
