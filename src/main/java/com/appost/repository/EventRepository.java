package com.appost.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.appost.model.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, UUID>{
    
    @Query(value = "SELECT * FROM event WHERE event_name = ?1 ", nativeQuery = true)
    Event searchEventByName(String eventName);

    @Query(value = "SELECT * FROM event WHERE id_organizer = ?1 ", nativeQuery = true)
    List<Event> searchEventsByOrganizer(String idOrganizer);
}
