package controllers;

import models.users.SalesManager;
import views.SalesManagerDashboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import services.SalesService;
import tables.DailySalesTableModel;
import tables.ItemNotOnSaleTableModel;
import tables.ItemOnSaleTableModel;
import tables.ItemSaleTableModel;

public class SalesManagerController extends BaseController {

    private SalesManagerDashboard dashboard;
    private SalesService salesService = new SalesService();

    // table models
    ItemOnSaleTableModel itemOnSaleTableModel = new ItemOnSaleTableModel();
    ItemNotOnSaleTableModel itemNotOnSaleTableModel = new ItemNotOnSaleTableModel();
    DailySalesTableModel saleTableModel = new DailySalesTableModel();
    ItemSaleTableModel itemSaleTableModel = new ItemSaleTableModel();

    // tables
    private JTable itemOnSaleTable;
    private JTable itemNotOnSaleTable;
    private JTable dailySalesTable;
    private JTable itemSaleTable;
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
        loadTables();
        updateTotalSaleAmount();
    }

    @Override
    protected void setupCustomListeners() {
        addSaleButtonListener();
    }

    private void loadTables() {
        // item tables
        itemOnSaleTable = dashboard.getItemOnSaleTable();
        itemNotOnSaleTable = dashboard.getItemNotOnSaleTable();
        itemOnSaleTable.setModel(itemOnSaleTableModel);
        itemNotOnSaleTable.setModel(itemNotOnSaleTableModel);

        // load daily sales table
        dailySalesTable = dashboard.getSalesTable();
        dailySalesTable.setModel(saleTableModel);

        // item sale table
        itemSaleTable = dashboard.getItemSaleTable();
        itemSaleTable.setModel(itemSaleTableModel);
    }

    private void updateTotalSaleAmount() {
        JLabel totalAmountText = dashboard.getTotalAmount();
        Double totalSale = salesService.calculateTodaySalesTotal();

        totalAmountText.setText(Double.toString(totalSale));
    }

    private void addSaleButtonListener() {
        JButton addSalesButton = dashboard.getAddSalesButton();

        addSalesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salesService.addSale(dashboard);

                saleTableModel.refresh();
                itemSaleTableModel.refresh();
                updateTotalSaleAmount();
            }
        });
    }
}
