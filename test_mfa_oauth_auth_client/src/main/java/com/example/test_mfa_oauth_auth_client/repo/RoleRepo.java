package com.example.test_mfa_oauth_auth_client.repo;

import com.example.test_mfa_oauth_auth_client.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
}
