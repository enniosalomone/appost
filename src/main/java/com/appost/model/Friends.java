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
@Table(name = "friends")
public class Friends {
    
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "usernamefriend1", updatable = true, nullable = false, columnDefinition = "VARCHAR(255)")
    private String usernameFriend1;

    @Column(name = "usernamefriend2", updatable = true, nullable = false, columnDefinition = "VARCHAR(255)")
    private String usernameFriend2;
    
    public Friends()
    {

    }
 
    public Friends(String usernameFriend1, String usernameFriend2)
    {
        this.usernameFriend1 = usernameFriend1;
        this.usernameFriend2 = usernameFriend2;
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
     * @return String return the usernameFriend1
     */
    public String getUsernameFriend1() {
        return usernameFriend1;
    }

    /**
     * @param usernameFriend1 the usernameFriend1 to set
     */
    public void setUsernameFriend1(String usernameFriend1) {
        this.usernameFriend1 = usernameFriend1;
    }

    /**
     * @return String return the usernameFriend2
     */
    public String getUsernameFriend2() {
        return usernameFriend2;
    }

    /**
     * @param usernameFriend2 the usernameFriend2 to set
     */
    public void setUsernameFriend2(String usernameFriend2) {
        this.usernameFriend2 = usernameFriend2;
    }

}
