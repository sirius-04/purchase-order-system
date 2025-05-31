/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.users;

import models.Identifiable;

/**
 *
 * @author Chan Yong Liang
 */

public abstract class User implements Identifiable {
    
    public enum Status {
        active,
        deleted
    }
    
    protected String userId;
    protected UserRole userRole;
    public String username;
    protected String password;
    protected Status status;
    
    public User(String userId, UserRole userRole, String username, String password, Status status) {
        this.userId = userId;
        this.userRole = userRole;
        this.username = username;
        this.password = password;
        this.status = status;
    }
    
    @Override
    public String getId() {
        return userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    
}
