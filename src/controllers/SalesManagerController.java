package controllers;

import helpers.*;
import models.users.SalesManager;
import views.SalesManagerDashboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import services.SalesService;

public class SalesManagerController extends BaseController {

    private SalesManagerDashboard dashboard;
    private SalesService salesService = new SalesService();

    // Tables
    private JTable itemOnSaleTable;
    private JTable itemNotOnSaleTable;
    private JTable dailySalesTable;
    private JTable purchaseOrderTable;
    private JTable historicalRequisitionTable;
    private JTable pendingRequisitionTable;
    private JTable supplierTable;

    public SalesManagerController(SalesManager salesManagerUser) {
        super(salesManagerUser);
    }

    @Override
    protected JFrame createView() {
        dashboard = new SalesManagerDashboard();
        return dashboard;
    }

    @Override
    protected void loadInitialData() {
        populateAllTables();
        updateTotalSaleAmount();
    }

    @Override
    protected void setupCustomListeners() {
        setupAddSaleListener();
    }

    private void populateAllTables() {
        populateItemTables();
        populateSalesTable();
        populatePurchaseOrderTable();
        populateRequisitionTables();
        populateSupplierTable();
    }

    private void populateItemTables() {
        itemOnSaleTable = dashboard.getItemOnSaleTable();
        itemNotOnSaleTable = dashboard.getItemNotOnSaleTable();
        ItemTableHelper.populateItemOnSale(itemOnSaleTable);
        ItemTableHelper.populateItemNotOnSale(itemNotOnSaleTable);
    }

    private void populateSalesTable() {
        dailySalesTable = dashboard.getSalesTable();
        DailySalesTableHelper.populateTodaySales(dailySalesTable);
    }

    private void populatePurchaseOrderTable() {
        purchaseOrderTable = dashboard.getOrderTable();
        PurchaseOrderTableHelper.populatePurchaseOrder(purchaseOrderTable);
    }

    private void populateRequisitionTables() {
        historicalRequisitionTable = dashboard.getHistoricalRequisitionTable();
        pendingRequisitionTable = dashboard.getPendingRequisitionTable();
        
        PurchaseRequisitionsTableHelper.populateAllRequisitions(historicalRequisitionTable, pendingRequisitionTable);
    }

    private void populateSupplierTable() {
        supplierTable = dashboard.getSupplierTable();
        SupplierTableHelper.populateSupplier(supplierTable);
    }

    private void setupAddSaleListener() {
        JButton addSalesButton = dashboard.getAddSalesButton();
        addSalesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salesService.addSale(dashboard, dashboard.getSalesTable());
                updateTotalSaleAmount();
            }
        });
    }
    
    private void updateTotalSaleAmount() {
        JLabel totalAmountText = dashboard.getTotalAmount();
        salesService.updateTotalSaleAmountLabel(totalAmountText);
    }
}
