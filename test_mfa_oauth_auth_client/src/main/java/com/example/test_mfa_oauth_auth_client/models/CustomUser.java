package com.example.test_mfa_oauth_auth_client.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
@Entity
@Table(name = "custom_user")
public class CustomUser implements Serializable {
    @Id
    @Column(name = "userId")
    private String account;
    private String password;
    private String active;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private List<Role> roleList;
}
