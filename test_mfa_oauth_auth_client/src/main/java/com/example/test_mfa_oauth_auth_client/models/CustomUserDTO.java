package com.example.test_mfa_oauth_auth_client.models;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CustomUserDTO implements Serializable {
    private String account;
    private String active;
    private List<Role> roleList;
}
