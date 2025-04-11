/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import models.users.SalesManager;
import views.SalesManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class SalesManagerController extends BaseController {
    public SalesManagerController(SalesManager sm) {
        super(sm);
    }
    
    @Override
    public void displayMenu() {
        SalesManagerDashboard dashboard = new SalesManagerDashboard();
        dashboard.setVisible(true);
    }
}
