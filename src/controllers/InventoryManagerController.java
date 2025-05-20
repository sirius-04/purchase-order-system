/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.swing.JFrame;
import models.users.InventoryManager;
import views.InventoryManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryManagerController extends BaseController {
    private InventoryManagerDashboard dashboard;
    
    public InventoryManagerController(InventoryManager user) {
        super(user);
    }

    @Override
    protected JFrame createView() {
        dashboard = new InventoryManagerDashboard();
        return dashboard;
    }

    @Override
    protected void loadInitialData() {
    }

    @Override
    protected void setupCustomListeners() {
    }


}
