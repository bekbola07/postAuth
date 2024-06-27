package org.example.postauth.repo;

import org.example.postauth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoleRepo extends JpaRepository<Role, UUID> {
    List<Role> findAllById(UUID id);
}
