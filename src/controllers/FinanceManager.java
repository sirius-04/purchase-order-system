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
public class FinanceManager extends User {
    public FinanceManager(String userId, String username, String password) {
        super(userId, username, password);
    }

    @Override
    public void displayMenu(User user) {

    }

    // role based function here
}
