package com.example.test_mfa_oauth_auth_client.models.security;

import com.example.test_mfa_oauth_auth_client.models.CustomUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * jwt decode value object
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo implements Serializable {
    private String issue;
    private String subject;

    private List<String> roleList;
    private String requestIp;
    private String deviceInfo;

    private CustomUserDTO customUserDTO;

    private Date expireDate;
}
