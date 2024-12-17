package com.ecommerce.ecommerce.repository;

import com.ecommerce.ecommerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , String>
{
    User findByEmail(String email);
}
