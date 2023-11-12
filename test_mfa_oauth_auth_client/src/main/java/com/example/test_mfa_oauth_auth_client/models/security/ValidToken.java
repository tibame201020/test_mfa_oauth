package com.example.test_mfa_oauth_auth_client.models.security;

import lombok.Data;

/**
 * valid token status object
 */
@Data
public class ValidToken {
    private TokenInfo tokenInfo;

    private boolean expired = false;
    private boolean decodeException = false;
    private boolean otherException = false;
    private String errorMessage;
}
