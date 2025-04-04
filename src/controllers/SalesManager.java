/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import models.User;
import views.SalesManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class SalesManager extends User {
    public SalesManager(String userId, String username, String password) {
        super(userId, username, password);
    }
    
    @Override
    public void displayMenu(User user) {
        if (user instanceof SalesManager) {
            SalesManager sm = (SalesManager) user;
            SalesManagerDashboard dashboard = new SalesManagerDashboard(sm);
            dashboard.setVisible(true);
        }
    }
    
    // role based function here
}
