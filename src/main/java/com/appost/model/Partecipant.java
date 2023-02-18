package com.appost.model;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "partecipant")
public class Partecipant implements Serializable{
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "idPartecipant", updatable = true, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID idPartecipant;
    
    @Column(name = "idEvent", updatable = true, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID idEvent;

    public Partecipant(){};

    public Partecipant(UUID idPartecipant, UUID idEvent)
    {
        this.idPartecipant = idPartecipant;
        this.idEvent = idEvent;
    }

    /**
     * @return UUID return the id of the relation
     */
    public UUID getId() {
        return id;
    }

    /**
     * @param id the id of the relation to set
     */
    public void setId(UUID id) {
        this.id = id;
    }
    /**
     * @return UUID return the idPartecipant
     */
    public UUID getIdPartecipant() {
        return idPartecipant;
    }

    /**
     * @param idPartecipant the idPartecipant to set
     */
    public void setIdPartecipant(UUID idPartecipant) {
        this.idPartecipant = idPartecipant;
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
