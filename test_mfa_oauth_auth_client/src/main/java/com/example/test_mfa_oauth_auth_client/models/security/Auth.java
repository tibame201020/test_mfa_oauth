package com.example.test_mfa_oauth_auth_client.models.security;

import com.example.test_mfa_oauth_auth_client.models.CustomUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * implement UserDetails
 * 對應spring security驗證完資料庫, 將自定義user轉成UserDetails
 * 便於驗證帳戶狀態
 * getAuthorities產roles, 需要主動添加ROLE_作為prefix
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Auth implements UserDetails {
    private CustomUser customUser;

    @Override
    public Set<GrantedAuthority> getAuthorities() {
        return customUser
                .getRoleList()
                .stream()
                .map(role -> "ROLE_" + role.getRoleName())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

    }

    @Override
    public String getPassword() {
        return this.customUser.getPassword();
    }

    @Override
    public String getUsername() {
        return this.customUser.getAccount();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return "2".equals(this.customUser.getActive());
    }
}
