package com.web.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.ecommerce.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Optional<User> findByUsername(String username);
    @Query(
        value="SELECT * FROM public.users WHERE username = :username",
        nativeQuery=true
    )
    Optional<User> findByUsername(@Param("username") String username);
  
}
