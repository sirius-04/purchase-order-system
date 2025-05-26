/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.swing.JFrame;
import javax.swing.JTable;
import models.users.FinanceManager;
import services.PurchaseOrderService;
import tables.HistoricalPurchaseRequisitionTableModel;
import tables.InventoryTableModel;
import tables.PaymentTableModel;
import tables.PendingPurchaseRequisitionTableModel;
import tables.PurchaseOrderTableModel;
import views.FinanceManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class FinanceManagerController extends BaseController {

    private FinanceManagerDashboard dashboard;
    private PurchaseOrderService poService = new PurchaseOrderService();

    // table models
    PaymentTableModel paymentTableModel = new PaymentTableModel();
    PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel();
    PendingPurchaseRequisitionTableModel pendingRequisitionTableModel = new PendingPurchaseRequisitionTableModel();
    HistoricalPurchaseRequisitionTableModel historicalRequisitionTableModel = new HistoricalPurchaseRequisitionTableModel();
    InventoryTableModel inventoryTableModel = new InventoryTableModel();

    private JTable purchaseOrderTable;
    private JTable inventoryTable;
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
        loadTables();
    }

    @Override
    protected void setupCustomListeners() {
        approvePOListener();
        poService.verifyUpdateListener(dashboard, inventoryTable);
        
    }

    private void loadTables() {
        // po table
        purchaseOrderTable = dashboard.getOrderTable();
        purchaseOrderTable.setModel(purchaseOrderTableModel);

        // payment table
        paymentTable = dashboard.getPaymentTable();
        paymentTable.setModel(paymentTableModel);

        // pr tables
        pendingRequisitionTable = dashboard.getPendingRequisitionTable();
        pendingRequisitionTable.setModel(pendingRequisitionTableModel);
        historicalRequisitionTable = dashboard.getHistoricalRequisitionTable();
        historicalRequisitionTable.setModel(historicalRequisitionTableModel);
        
        // inventory table
        inventoryTable = dashboard.getInventoryTable();
        inventoryTable.setModel(inventoryTableModel);
    }
    
    private void approvePOListener() {
        poService.addApprovalListener(dashboard, purchaseOrderTable);
        purchaseOrderTableModel.refresh();
    }
}
