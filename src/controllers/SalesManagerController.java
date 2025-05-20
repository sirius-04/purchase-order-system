/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.swing.JFrame;
import javax.swing.JTable;
import models.users.SalesManager;
import views.SalesManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class SalesManagerController extends BaseController {
    
     private SalesManagerDashboard dashboard;

    public SalesManagerController(SalesManager salesManagerUser) {
        super(salesManagerUser);
    }

    @Override
    protected JFrame createView() {
        dashboard = new SalesManagerDashboard();
        return dashboard;
    }
    
    @Override
    protected void loadInitialData() {
        JTable dailySalesTable = dashboard.getjTable5();
    }
    
    @Override
    protected void setupCustomListeners() {
        
    }
}
