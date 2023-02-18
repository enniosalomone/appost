package com.appost.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.appost.model.FriendsForEvent;

@Repository
public interface FriendForEventsRepository extends JpaRepository<FriendsForEvent, UUID>{
    
    @Query(value = "SELECT * FROM friendsforevent WHERE (id_partecipant1 = ?1 OR id_partecipant2 = ?1) AND id_event = ?2", nativeQuery = true)
    List<FriendsForEvent> searchFrindsToEvent(String idUser, String idEvent);

    @Modifying
    @Query(value = "DELETE FROM friendsforevent WHERE id_event=?1", nativeQuery = true)
    void eventDeleted(String idEvent);

    @Query(value = "SELECT COUNT(*) FROM friendsforevent WHERE ((id_partecipant1 = ?1 OR id_partecipant2 = ?1) AND id_event = ?2)",nativeQuery = true)
    int calculatePercDisc(String idPartecipant, String idEvent);
}