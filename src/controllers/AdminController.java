/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTable;
import models.Supplier;
import models.users.Admin;
import models.users.User;
import models.users.UserRole;
import services.ItemService;
import services.SupplierService;
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
    private SupplierService supplierService = new SupplierService();
    
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
        changeUsernameOrPassword();
        addItemButtonListener();
        setupSupplierListeners();
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
    
    private void changeUsernameOrPassword() {
        userTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt){
                int row = userTable.getSelectedRow();
                if (row != -1) {
                    UserTableModel userModel = (UserTableModel) userTable.getModel();
                    User user = userModel.getUserAt(row);
                    
                    userService.renameUserOrChangePassword(dashboard, user);
                    userModel.refresh();    
                }
            }
        });
    }
    
   private void setupSupplierListeners() {
        SupplierTableModel supplierModel = (SupplierTableModel) supplierTable.getModel();
        dashboard.getAddSupplierButton().addActionListener(e -> {
            supplierService.addSupplier(dashboard);

            supplierModel.refresh();
        });

        dashboard.getSupplierTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = dashboard.getSupplierTable().getSelectedRow();
                Supplier selectedSupplier = supplierTableModel.getSupplierAt(row);
                if (selectedSupplier != null) {
                    supplierService.displaySupplierDetails(dashboard, selectedSupplier);

                    supplierModel.refresh();
                }
            }
        });
    }
}
