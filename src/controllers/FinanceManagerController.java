/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.swing.JFrame;
import javax.swing.JTable;
import models.InventoryUpdate;
import models.Payment;
import models.users.FinanceManager;
import services.PurchaseOrderService;
import tables.HistoricalPurchaseRequisitionTableModel;
import tables.InventoryUpdateTableModel;
import tables.PaymentTableModel;
import tables.PendingPurchaseRequisitionTableModel;
import tables.PurchaseOrderTableModel;
import views.FinanceManagerDashboard;
import models.PurchaseOrder;

/**
 *
 * @author Chan Yong Liang
 */
public class FinanceManagerController extends BaseController {

    private FinanceManagerDashboard dashboard;
    private PurchaseOrderService poService = new PurchaseOrderService();

    // table models
    PaymentTableModel paymentTableModel = new PaymentTableModel();
    PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel(PurchaseOrder.Status.pending, true);
    PendingPurchaseRequisitionTableModel pendingRequisitionTableModel = new PendingPurchaseRequisitionTableModel();
    HistoricalPurchaseRequisitionTableModel historicalRequisitionTableModel = new HistoricalPurchaseRequisitionTableModel();
    InventoryUpdateTableModel inventoryTableModel = new InventoryUpdateTableModel();

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
        verifyUpdateListener();
        processPaymentListener();
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
       purchaseOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = purchaseOrderTable.getSelectedRow();
                if (row != -1) {
                   PurchaseOrderTableModel purchaseOrderModel = (PurchaseOrderTableModel) purchaseOrderTable.getModel();
                   InventoryUpdateTableModel inventoryModel = (InventoryUpdateTableModel) inventoryTable.getModel();
                   PurchaseOrder selectedPO = purchaseOrderModel.getPurchaseOrderAt(row);
                   
                   poService.addApprovalListener(dashboard, selectedPO);
                   purchaseOrderModel.refresh();
                   inventoryModel.refresh();
                }
            }
        });
    }
    
    private void verifyUpdateListener() {
        inventoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = inventoryTable.getSelectedRow();
                if (row != -1) {
                    InventoryUpdateTableModel inventoryModel = (InventoryUpdateTableModel) inventoryTable.getModel();
                    PaymentTableModel paymentModel = (PaymentTableModel) paymentTable.getModel();
                    InventoryUpdate inventory = inventoryModel.getInventoryUpdateAt(row);

                    poService.verifyUpdate(dashboard, inventory);
                    inventoryModel.refresh();
                    paymentModel.refresh();
                    }
                }
            }); 
    } 
    
    private void processPaymentListener() {
        paymentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = paymentTable.getSelectedRow();
                if (row != -1) {
                    PaymentTableModel paymentModel = (PaymentTableModel) paymentTable.getModel();
                    Payment selectedPayment = paymentModel.getPaymentAt(row);
                    
                poService.processPayment(dashboard, selectedPayment);
                paymentModel.refresh();
                }
            }
        });
    }
}


        
