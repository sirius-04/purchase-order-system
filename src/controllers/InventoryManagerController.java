/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import models.users.InventoryManager;
import views.InventoryManagerDashboard;
import tables.ItemTableModel;
import tables.PurchaseOrderTableModel;
import utils.LowStockRenderer;
import models.PurchaseOrder;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryManagerController extends BaseController {
    private InventoryManagerDashboard dashboard;
    
     // table models
    ItemTableModel itemTableModel = new ItemTableModel();
    PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel(PurchaseOrder.Status.approved, false);

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
    
        // Status selection
        JComboBox<String> statusComboBox = dashboard.getStatusComboBox();
        statusComboBox.addActionListener(e -> {
            String selected = (String) statusComboBox.getSelectedItem();

            switch (selected) {
                case "On Sale":
                    itemTableModel.setStatus(ItemTableModel.Status.ON_SALE);
                    break;
                case "Not On Sale":
                    itemTableModel.setStatus(ItemTableModel.Status.NOT_ON_SALE);
                    break;
                default:
                    itemTableModel.setStatus(ItemTableModel.Status.ALL);
                    break;
            }
        });

        // Search input
        JTextField searchInput = dashboard.getItemSearchInput();
        searchInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = searchInput.getText();
                    itemTableModel.searchByName(inputText);
                }
            }
        });
    }
    
    private void loadTables() {
        // item table
        itemTable = dashboard.getItemTable();
        itemTable.setModel(itemTableModel);
        
        // Render low stock items
        LowStockRenderer rowRenderer = new LowStockRenderer(3);
        for (int i = 0; i < itemTable.getColumnCount(); i++) {
            itemTable.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }
        
        // po table
        purchaseOrderTable = dashboard.getOrderTable();
        purchaseOrderTable.setModel(purchaseOrderTableModel);

    }
}
