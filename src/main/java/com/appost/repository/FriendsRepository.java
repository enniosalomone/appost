package com.appost.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.appost.model.Friends;

@Repository
public interface FriendsRepository extends JpaRepository<Friends, UUID>{

    @Query(value = "SELECT * FROM friends WHERE usernamefriend1 = ?1 OR usernamefriend2 = ?1", nativeQuery = true)
    List<Friends> gerFriendList(String usernameFriend);
    
}