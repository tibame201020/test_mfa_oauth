package com.example.test_mfa_oauth_auth_client.repo;

import com.example.test_mfa_oauth_auth_client.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
}
