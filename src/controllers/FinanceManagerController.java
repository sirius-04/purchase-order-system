/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import helpers.PaymentTableHelper;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import models.users.FinanceManager;
import views.FinanceManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class FinanceManagerController extends BaseController {
    private FinanceManagerDashboard dashboard;
    
    private JTable paymentTable;
    
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
        paymentTable = dashboard.getPaymentTable();
        DefaultTableModel orderModel = (DefaultTableModel) paymentTable.getModel();
        PaymentTableHelper.populatePayment(orderModel);
    }

    @Override
    protected void setupCustomListeners() {
    }
}
