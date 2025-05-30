package controllers;

import models.users.SalesManager;
import views.SalesManagerDashboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import models.PurchaseRequisition;
import models.Sales;
import services.ItemService;
import services.PurchaseRequisitionService;
import services.SalesService;
import tables.DailySalesTableModel;
import tables.HistoricalPurchaseRequisitionTableModel;
import tables.ItemNotOnSaleTableModel;
import tables.ItemOnSaleTableModel;
import tables.ItemSaleTableModel;
import tables.PendingPurchaseRequisitionTableModel;

public class SalesManagerController extends BaseController {

    private SalesManagerDashboard dashboard;
    private SalesService salesService = new SalesService();
    private final PurchaseRequisitionService requisitionService = new PurchaseRequisitionService();
    private ItemService itemService = new ItemService();
    private final PendingPurchaseRequisitionTableModel pendingRequisitionTableModel = new PendingPurchaseRequisitionTableModel();
    private final HistoricalPurchaseRequisitionTableModel historicalRequisitionTableModel = new HistoricalPurchaseRequisitionTableModel();

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
        salePanelListener();
        itemPanelListener();
        addRequisitionButtonListener();
        addPendingRequisitionTableListener();
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
        
         pendingRequisitionTable = dashboard.getPendingRequisitionTable();
        historicalRequisitionTable = dashboard.getHistoricalRequisitionTable();

        pendingRequisitionTable.setModel(pendingRequisitionTableModel);
        historicalRequisitionTable.setModel(historicalRequisitionTableModel);
    }

    private void salePanelListener() {
        addSaleButtonListener();
        addSaleTableListener();
    }

    private void itemPanelListener() {
        addItemButtonListener();
    }
    
    private void addSaleButtonListener() {
        JButton addSalesButton = dashboard.getAddSalesButton();

        addSalesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salesService.addSale(dashboard);

                refreshDailySalePanel();
                refreshItemPanel();
            }
        });
    }

    private void addSaleTableListener() {
        dailySalesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = dailySalesTable.getSelectedRow();
                if (row != -1) {
                    Sales selectedSale = saleTableModel.getSalesAt(row);
                    if (selectedSale != null) {
                        salesService.displaySaleDetails(dashboard, selectedSale);
                        refreshDailySalePanel();
                        refreshItemPanel();
                    }
                }
            }
        });
    }

    private void addItemButtonListener() {
        JButton addItemButton = dashboard.getAddItemButton();

        addItemButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                itemService.addItem(dashboard);

                refreshDailySalePanel();
                refreshItemPanel();
            }
        });
    }
    
    private void updateTotalSaleAmount() {
        JLabel totalAmountText = dashboard.getTotalAmount();
        Double totalSale = salesService.calculateTodaySalesTotal();

        totalAmountText.setText(Double.toString(totalSale));
    }
 
    private void refreshDailySalePanel() {
        saleTableModel.refresh();
        itemSaleTableModel.refresh();
        updateTotalSaleAmount();
    }

    private void refreshItemPanel() {
        itemOnSaleTableModel.refresh();
        itemNotOnSaleTableModel.refresh();
    }
    private void addRequisitionButtonListener() {
        JButton addRequisitionButton = dashboard.getAddRequisitionButton();

        addRequisitionButton.addActionListener(e -> {
            requisitionService.showCreateForm(dashboard, () -> {
                pendingRequisitionTableModel.refresh();
                historicalRequisitionTableModel.refresh();
            });
        });
    }
    private void addPendingRequisitionTableListener() {
        pendingRequisitionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                int row = pendingRequisitionTable.getSelectedRow();
                if (row != -1) {
                    PurchaseRequisition requisition = pendingRequisitionTableModel.getRequisitionAt(row);
                    if (requisition != null) {
                        requisitionService.showActionPanel(requisition, dashboard, () -> {
                            pendingRequisitionTableModel.refresh();
                            historicalRequisitionTableModel.refresh();
                        });
                    }
                }
            }
        });
    }
}