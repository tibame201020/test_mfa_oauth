package com.example.test_mfa_oauth_auth_client.utils;

import com.example.test_mfa_oauth_auth_client.models.CustomUserDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

/**
 * global get spring security authentication
 * 可取得使用者參數在後端處理資料時期使用
 * from PreSecurityCheckJsonWebToken valid token: set to Authentication
 */
public class SecurityContextHandler {
    public static CustomUserDTO getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication)) {
            return null;
        }
        return (CustomUserDTO) authentication.getDetails();
    }
}
