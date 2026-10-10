package com.kamsan.ticketservice.repository;

import com.kamsan.ticketservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRolePublicId(UUID publicId);
}
