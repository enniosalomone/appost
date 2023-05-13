package com.appost.repository;

import org.springframework.stereotype.Repository;

import com.appost.model.Partecipant;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface PartecipantRepository extends JpaRepository<Partecipant, UUID>{
    
    @Query(value = "SELECT * FROM partecipant WHERE id_partecipant = ?1 AND id_event=?2", nativeQuery = true)
    Partecipant searchPartecipantToEvent(String idPartecipant, String idEvent);
    
    @Modifying
    @Query(value = "DELETE FROM partecipant WHERE id_event=?1", nativeQuery = true)
    void eventDeleted(String idEvent);

    @Query(value = "SELECT * FROM partecipant WHERE id_partecipant=?1", nativeQuery = true)
    List<Partecipant> getSignedUpEventForSpecificUser(String idPartecipant);

    @Query(value = "SELECT * FROM partecipant WHERE id_event=?1", nativeQuery = true)
    List<Partecipant> getAllPartecipantToEvent(String idEvent);
}