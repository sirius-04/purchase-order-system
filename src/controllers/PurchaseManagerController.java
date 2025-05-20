/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.swing.JFrame;
import models.users.PurchaseManager;
import views.PurchaseManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class PurchaseManagerController extends BaseController {
    
    private PurchaseManagerDashboard dashboard;
    
    public PurchaseManagerController(PurchaseManager user) {
        super(user);
    }

    @Override
    protected JFrame createView() {
        dashboard = new PurchaseManagerDashboard();
        return dashboard;
    }

    @Override
    protected void loadInitialData() {
    }

    @Override
    protected void setupCustomListeners() {
    }
    
    
}
