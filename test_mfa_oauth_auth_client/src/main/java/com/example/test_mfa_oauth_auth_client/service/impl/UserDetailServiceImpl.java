package com.example.test_mfa_oauth_auth_client.service.impl;

import com.example.test_mfa_oauth_auth_client.models.CustomUser;
import com.example.test_mfa_oauth_auth_client.models.security.Auth;
import com.example.test_mfa_oauth_auth_client.repo.CustomUserRepo;
import com.example.test_mfa_oauth_auth_client.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 作為實作UserDetailsService
 * 必須Override loadUserByUsername 提供spring security驗證account
 */
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailService {
    private final CustomUserRepo customUserRepo;

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        CustomUser customUser = customUserRepo.findById(account)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new Auth(customUser);
    }
}
