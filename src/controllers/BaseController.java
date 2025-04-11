/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import models.users.User;

/**
 *
 * @author Chan Yong Liang
 */
public abstract class BaseController {
    protected User user;
    
    public BaseController(User user) {
        this.user = user;
    }
    
    public abstract void displayMenu();
}
