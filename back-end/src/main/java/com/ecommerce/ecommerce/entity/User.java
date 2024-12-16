package com.ecommerce.ecommerce.entity;

import com.ecommerce.ecommerce.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="users")
public class User
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private UserRole role;

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] img;
}
