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

    // Getter for username (which is the email in your case)
    // Getter for password (explicitly defining it here, just in case Lombok doesn't work)
    public String getPassword() {
        return this.password;
    }
    public String getUsername() {
        return this.name;
    }
    public String getEmail() {
        return this.name;
    }

    // Getter for roles (single role)
    public String getRole() {
        return this.role.name();  // Return the role as a string (assuming it's an enum)
    }
    // Default constructor
    public User() {}

    // Getters and setters for each field

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // Add setter for role
    public void setRole(UserRole role) {
        this.role = role;
    }
}
