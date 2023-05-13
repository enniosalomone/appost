package com.appost.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.appost.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID>{
    
    @Query(value = "SELECT id FROM user", nativeQuery = true)
    List<UUID> /*List<?>*/ getIDList();

    @Query(value = "SELECT * FROM user WHERE username = ?1 ", nativeQuery = true)
    User searchUserByUsername(String username);

    @Query(value = "SELECT username FROM user", nativeQuery = true)
    List<String> getUsernameList();

    @Query(value = "SELECT * FROM user WHERE role = ?1 ", nativeQuery = true)
    List<User> getBusinessList(int role);

    @Query(value = "SELECT * FROM user WHERE email = ?1 ", nativeQuery = true)
    User searchUserByEmail(String email);
    
}
