/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JTable;
import models.users.PurchaseManager;
import services.PurchaseOrderService;
import services.UserService;
import tables.HistoricalPurchaseRequisitionTableModel;
import tables.ItemNotOnSaleTableModel;
import tables.ItemOnSaleTableModel;
import tables.PendingPurchaseRequisitionTableModel;
import tables.PurchaseOrderTableModel;
import tables.SupplierTableModel;
import views.PurchaseManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class PurchaseManagerController extends BaseController {

    private PurchaseManagerDashboard dashboard;
    private UserService userService = new UserService();
    
    // table models
    ItemOnSaleTableModel itemOnSaleTableModel = new ItemOnSaleTableModel();
    ItemNotOnSaleTableModel itemNotOnSaleTableModel = new ItemNotOnSaleTableModel();
    HistoricalPurchaseRequisitionTableModel HistoricalPurchaseRequisitionTableModel = new HistoricalPurchaseRequisitionTableModel();
    PendingPurchaseRequisitionTableModel PendingPurchaseRequisitionTableModel = new PendingPurchaseRequisitionTableModel();
    PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel(PurchaseOrderTableModel.POStatus.ALL);
    SupplierTableModel SupplierTableModel = new SupplierTableModel();
    //table
    private JTable itemOnSaleTable;
    private JTable itemNotOnSaleTable;
    private JTable purchaseOrderTable;
    private JTable historicalRequisitionTable;
    private JTable pendingRequisitionTable;
    private JTable supplierTable;

    public PurchaseManagerController(PurchaseManager user) {
        super(user);
        System.out.println(this.currentUser.getUserId());
    }

    @Override
    protected JFrame createView() {
        dashboard = new PurchaseManagerDashboard();
        return dashboard;
    }

    @Override
    protected void loadInitialData() {
        loadTables();
    }

    @Override
    protected void setupCustomListeners() {
        POListener();
        logOut();
    }

    //refresh
    private void refreshItemPanel() {
        itemOnSaleTableModel.refresh();
        itemNotOnSaleTableModel.refresh();
    }

    private void refreshPRPanel() {
        HistoricalPurchaseRequisitionTableModel.refresh();
        PendingPurchaseRequisitionTableModel.refresh();
    }

    public void refreshPOPanel() {
        purchaseOrderTableModel.refresh();
    }

    private void loadTables() {
        // item tables
        itemOnSaleTable = dashboard.getItemOnSaleTable();
        itemNotOnSaleTable = dashboard.getItemNotOnSaleTable();
        itemOnSaleTable.setModel(itemOnSaleTableModel);
        itemNotOnSaleTable.setModel(itemNotOnSaleTableModel);

        //PR table
        historicalRequisitionTable = dashboard.getHistoricalRequisitionTable();
        pendingRequisitionTable = dashboard.getPendingRequisitionTable();
        historicalRequisitionTable.setModel(HistoricalPurchaseRequisitionTableModel);
        pendingRequisitionTable.setModel(PendingPurchaseRequisitionTableModel);

        //PO table
        purchaseOrderTable = dashboard.getOrderTable();
        purchaseOrderTable.setModel(purchaseOrderTableModel);

        //supplier table
        supplierTable = dashboard.getSupplierTable();
        supplierTable.setModel(SupplierTableModel);
    }

    //listener
    private void POListener() {
        setupGeneratePOListener();
        setupPOClickListener();
    }

    private void setupGeneratePOListener() {
        PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
        PurchaseManager currentManager = (PurchaseManager) currentUser;

        purchaseOrderService.setupGeneratePOListener(
                dashboard.getPendingRequisitionTable(),
                currentManager,
                () -> {
                    refreshPOPanel();
                    refreshPRPanel();
                }
        );
    }

    private void setupPOClickListener() {
        PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
        PurchaseManager currentManager = (PurchaseManager) currentUser;

        purchaseOrderService.setupPOTableClickListener(
                purchaseOrderTable,
                currentManager,
                () -> refreshPOPanel()
        );
    }
    
     private void logOut() {
       dashboard.getLogOut().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                userService.userLogOut(dashboard, currentUser);
            }
        });
    }

}
