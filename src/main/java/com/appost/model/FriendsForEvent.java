package com.appost.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "friendsforevent")
public class FriendsForEvent {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "idPartecipant1", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID idPartecipant1;

    @Column(name = "idPartecipant2", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID idPartecipant2;

    @Column(name = "idEvent", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID idEvent;

    

    /**
     * @return UUID return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * @return UUID return the idPartecipant1
     */
    public UUID getIdPartecipant1() {
        return idPartecipant1;
    }

    /**
     * @param idPartecipant1 the idPartecipant1 to set
     */
    public void setIdPartecipant1(UUID idPartecipant1) {
        this.idPartecipant1 = idPartecipant1;
    }

    /**
     * @return UUID return the idPartecipant2
     */
    public UUID getIdPartecipant2() {
        return idPartecipant2;
    }

    /**
     * @param idPartecipant2 the idPartecipant2 to set
     */
    public void setIdPartecipant2(UUID idPartecipant2) {
        this.idPartecipant2 = idPartecipant2;
    }

    /**
     * @return UUID return the idEvent
     */
    public UUID getIdEvent() {
        return idEvent;
    }

    /**
     * @param idEvent the idEvent to set
     */
    public void setIdEvent(UUID idEvent) {
        this.idEvent = idEvent;
    }

}
