package com.nelson.tms.repository;

import com.nelson.tms.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
