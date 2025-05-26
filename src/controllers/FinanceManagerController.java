/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import helpers.ItemTableHelper;
import helpers.PaymentTableHelper;
import helpers.PurchaseOrderTableHelper;
import helpers.PurchaseRequisitionsTableHelper;
import javax.swing.JFrame;
import javax.swing.JTable;
import models.PurchaseOrder;
import models.users.FinanceManager;
import services.PurchaseOrderService;
import views.FinanceManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class FinanceManagerController extends BaseController {
    private FinanceManagerDashboard dashboard;
    
    private JTable purchaseOrderTable;
    private JTable itemTable;
    private JTable paymentTable;
    private JTable historicalRequisitionTable;
    private JTable pendingRequisitionTable;
    
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
        populateAllTables();
    }

    @Override
    protected void setupCustomListeners() {
        PurchaseOrderService service = new PurchaseOrderService();
        service.addApprovalListener(dashboard, purchaseOrderTable);
    }
    
    private void populateAllTables() {
        populatePurchaseOrderTables();
        populateInventoryTables();
        populatePaymentTables();
        populateRequisitionTables();
    }
    
    private void populatePurchaseOrderTables() {
        purchaseOrderTable = dashboard.getOrderTable();
        PurchaseOrderTableHelper.populatePurchaseOrder(purchaseOrderTable, PurchaseOrder.Status.pending);
    }
    
    private void populateInventoryTables() {
        itemTable = dashboard.getInventoryTable();
        ItemTableHelper.populateItemOnSale(itemTable);
        ItemTableHelper.populateItemNotOnSale(itemTable);
    }
    
    private void populatePaymentTables() {
        paymentTable = dashboard.getPaymentTable();
        PaymentTableHelper.populatePayment(paymentTable);
    }
    
    private void populateRequisitionTables() {
        historicalRequisitionTable = dashboard.getHistoricalRequisitionTable();
        pendingRequisitionTable = dashboard.getPendingRequisitionTable();
        
        PurchaseRequisitionsTableHelper.populateAllRequisitions(historicalRequisitionTable, pendingRequisitionTable);
    }

}
