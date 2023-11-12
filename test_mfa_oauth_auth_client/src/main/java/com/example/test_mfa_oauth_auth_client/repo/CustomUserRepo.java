package com.example.test_mfa_oauth_auth_client.repo;

import com.example.test_mfa_oauth_auth_client.models.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomUserRepo extends JpaRepository<CustomUser, String> {
}
