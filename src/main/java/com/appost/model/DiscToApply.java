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
@Table(name = "disctoapply")
public class DiscToApply {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;
    
    private String username;
    private int percentToDisc;
    private String businessUser;

    public DiscToApply(){

    }

    public DiscToApply (String username, int percentToDisc, String businessUser){
        this.username = username;
        this.percentToDisc = percentToDisc;
        this.businessUser = businessUser;
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
     * @return String return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return int return the percentToDisc
     */
    public int getPercentToDisc() {
        return percentToDisc;
    }

    /**
     * @param percentToDisc the percentToDisc to set
     */
    public void setPercentToDisc(int percentToDisc) {
        this.percentToDisc = percentToDisc;
    }


    /**
     * @return String return the businessUser
     */
    public String getBusinessUser() {
        return businessUser;
    }

    /**
     * @param businessUser the businessUser to set
     */
    public void setBusinessUser(String businessUser) {
        this.businessUser = businessUser;
    }

}