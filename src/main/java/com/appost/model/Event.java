package com.appost.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "event")
public class Event {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;
    private String eventName;
    private String address;
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "idOrganizer", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID idOrganizer;
    private String nameOrganizer;
    private String date;
    

    public Event (){

    }
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
     * @return String return the eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * @param eventName the eventName to set
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * @return String return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return User return the organizer
     */
    public UUID getOrganizer() {
        return idOrganizer;
    }

    /**
     * @param organizer the organizer to set
     */
    public void setOrganizer(UUID idOrganizer) {
            this.idOrganizer = idOrganizer;
    }

    /**
     * @return Date return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(String date) {
        this.date= date;
    }


    /**
     * @return UUID return the idOrganizer
     */
    public UUID getIdOrganizer() {
        return idOrganizer;
    }

    /**
     * @param idOrganizer the idOrganizer to set
     */
    public void setIdOrganizer(UUID idOrganizer) {
        this.idOrganizer = idOrganizer;
    }

    /**
     * @return String return the nameOrganizer
     */
    public String getNameOrganizer() {
        return nameOrganizer;
    }

    /**
     * @param nameOrganizer the nameOrganizer to set
     */
    public void setNameOrganizer(String nameOrganizer) {
        this.nameOrganizer = nameOrganizer;
    }

}
