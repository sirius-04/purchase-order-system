/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import models.users.Admin;
import models.users.UserRole;
import services.ItemService;
import services.UserService;
import tables.HistoricalPurchaseRequisitionTableModel;
import tables.ItemNotOnSaleTableModel;
import tables.ItemOnSaleTableModel;
import tables.PendingPurchaseRequisitionTableModel;
import tables.PurchaseOrderTableModel;
import tables.SupplierTableModel;
import tables.UserTableModel;
import views.AdminDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class AdminController extends BaseController {
    
    private AdminDashboard dashboard;
    private ItemService itemService = new ItemService();
    private UserService userService = new UserService();
    
    UserTableModel userTableModel = new UserTableModel();
    ItemOnSaleTableModel itemOnSaleTableModel = new ItemOnSaleTableModel();
    ItemNotOnSaleTableModel itemNotOnSaleTableModel = new ItemNotOnSaleTableModel();
    SupplierTableModel supplierTableModel = new SupplierTableModel();
    PendingPurchaseRequisitionTableModel pendingRequisitionTableModel = new PendingPurchaseRequisitionTableModel();
    HistoricalPurchaseRequisitionTableModel historicalRequisitionTableModel = new HistoricalPurchaseRequisitionTableModel();
    PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel(PurchaseOrderTableModel.POStatus.ALL);
    
    private JTable userTable;
    private JTable itemOnSaleTable;
    private JTable itemNotOnSaleTable;
    private JTable supplierTable;
    private JTable pendingPRTable;
    private JTable historicalPRTable;
    private JTable purchaseOrderTable;
    
    
    public AdminController(Admin adminUser) {
        super(adminUser);
    }

    @Override
    protected JFrame createView() {
        dashboard = new AdminDashboard();
        return dashboard;
    }

    @Override
    protected void loadInitialData() {
        loadTables();
    }

    @Override
    protected void setupCustomListeners() {
        registerNewUser();
        addItemButtonListener();
    }
    
    private void loadTables() {
        userTable = dashboard.getUserTable();
        userTable.setModel(userTableModel);

        itemOnSaleTable = dashboard.getItemOnSaleTable();
        itemOnSaleTable.setModel(itemOnSaleTableModel);
        itemNotOnSaleTable = dashboard.getItemNotOnSaleTable();
        itemNotOnSaleTable.setModel(itemNotOnSaleTableModel);
        
        supplierTable = dashboard.getSupplierTable();
        supplierTable.setModel(supplierTableModel);
        
        pendingPRTable = dashboard.getPendingPRTable();
        pendingPRTable.setModel(pendingRequisitionTableModel);
        historicalPRTable = dashboard.getHistoricalPRTable();
        historicalPRTable.setModel(historicalRequisitionTableModel);
        
        purchaseOrderTable = dashboard.getPOTable();
        purchaseOrderTable.setModel(purchaseOrderTableModel);
    }
    
    private void addItemButtonListener() {
        JButton addItemButton = dashboard.getAddItemButton();

        addItemButton.addActionListener((ActionEvent e) -> {
            itemService.addItem(dashboard);
            
            refreshItemPanel();
        });
    }
    
    private void refreshItemPanel() {
        itemOnSaleTableModel.refresh();
        itemNotOnSaleTableModel.refresh();
    }
    
    private void registerNewUser() {
        JButton registerUserButton = dashboard.getCreateUserButton();
        
        registerUserButton.addActionListener((ActionEvent e) -> {
            String username = dashboard.getUsername().getText();
            String password = dashboard.getPassword().getText();
            String roleToString = (String) dashboard.getSelectedRole().getSelectedItem();
            
            UserRole role = UserRole.valueOf(roleToString);
            
            userService.registerUser(dashboard, username, password, role);
            
            userTableModel.refresh();
        });
    }
}
