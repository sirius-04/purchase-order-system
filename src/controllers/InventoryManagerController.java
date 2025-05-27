/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.swing.JFrame;
import javax.swing.JTable;
import models.users.InventoryManager;
import views.InventoryManagerDashboard;
import tables.ItemTableModel;
import tables.PurchaseOrderTableModel;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryManagerController extends BaseController {
    private InventoryManagerDashboard dashboard;
    
     // table models
    ItemTableModel itemTableModel = new ItemTableModel();
     PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel();

    // tables
    private JTable itemTable;
    private JTable purchaseOrderTable;
    
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
        loadTables();
    }

    @Override
    protected void setupCustomListeners() {
    }
    
    private void loadTables() {
        // item tables
        itemTable = dashboard.getItemTable();
        itemTable.setModel(itemTableModel);
        
        // po table
        purchaseOrderTable = dashboard.getOrderTable();
        purchaseOrderTable.setModel(purchaseOrderTableModel);

    }
}
