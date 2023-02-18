package com.appost.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.UUID;;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    @Type(type = "uuid-char")
    private UUID id;
    private String name;
    private String surname;
    private String username;
    private String password;
    private String email;
    private Roles role;
    private int percentageDisc;
    private String address;
    private boolean resetPasswordRequest;

    public User(){

    }
    public User(String name, String surname, String username, String email, Roles role,
        int percentageDisc)
    {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.role = role;
        this.percentageDisc = percentageDisc;
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
     * @return UUID return the id
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param id the id to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * @return String return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return Roles return the role
     */
    public Roles getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(Roles role) {
        this.role = role;
    }

    /**
     * @return int return the percentageDisc
     */
    public int getPercentageDisc() {
        return percentageDisc;
    }

    /**
     * @param percentageDisc the percentageDisc to set
     */
    public void setPercentageDisc(int percentageDisc) {
        this.percentageDisc = percentageDisc;
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

    public void increasePercentageDisc(int increase)
    {
        percentageDisc += increase;
    }
    
    public boolean decreasePercDisc(int decrease)
    {
        if(percentageDisc - decrease >= 0){
            percentageDisc -= decrease;
            return true;
        }
        else
            return false;
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
     * @return boolean return the resetPasswordRequest
     */
    public boolean isResetPasswordRequest() {
        return resetPasswordRequest;
    }

    /**
     * @param resetPasswordRequest the resetPasswordRequest to set
     */
    public void setResetPasswordRequest(boolean resetPasswordRequest) {
        this.resetPasswordRequest = resetPasswordRequest;
    }

}
