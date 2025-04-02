/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import models.User;

/**
 *
 * @author Chan Yong Liang
 */
public class Admin extends User {
    public Admin(String userId, String username, String password) {
        super(userId, username, password);
    }

    @Override
    public void displayMenu() {
    }
    
    // role based functions here
}
