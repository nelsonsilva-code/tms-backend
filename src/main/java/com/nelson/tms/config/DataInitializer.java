package com.nelson.tms.config;

import com.nelson.tms.entity.Role;
import com.nelson.tms.entity.Permission;
import com.nelson.tms.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(null, "ROLE_ADMIN",
                    EnumSet.allOf(Permission.class)));
            roleRepository.save(new Role(null, "ROLE_MANAGER",
                    EnumSet.of(Permission.CREATE, Permission.READ, Permission.UPDATE)));
            roleRepository.save(new Role(null, "ROLE_USER",
                    EnumSet.of(Permission.READ)));
        }
    }
}
