/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.swing.JFrame;
import models.users.FinanceManager;
import views.FinanceManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class FinanceManagerController extends BaseController {
    private FinanceManagerDashboard dashboard;
    
    public FinanceManagerController(FinanceManager user) {
        super(user);
    } 

    @Override
    protected JFrame createView() {
        dashboard = new FinanceManagerDashboard();
        return dashboard;
    }

    @Override
    protected void loadInitialData() {
    }

    @Override
    protected void setupCustomListeners() {
    }
}
