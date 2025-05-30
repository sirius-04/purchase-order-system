package controllers;

import models.users.SalesManager;
import models.Sales;
import services.ItemService;
import services.SalesService;
import tables.*;
import views.SalesManagerDashboard;

import javax.swing.*;
import java.awt.event.*;

public class SalesManagerController extends BaseController {

    private final SalesManagerDashboard dashboard;
    private final SalesService salesService = new SalesService();
    private final ItemService itemService = new ItemService();

    // Table Models
    private final ItemOnSaleTableModel itemOnSaleTableModel = new ItemOnSaleTableModel();
    private final ItemNotOnSaleTableModel itemNotOnSaleTableModel = new ItemNotOnSaleTableModel();
    private final DailySalesTableModel saleTableModel = new DailySalesTableModel();
    private final ItemSaleTableModel itemSaleTableModel = new ItemSaleTableModel();
    private final SupplierTableModel supplierTableModel = new SupplierTableModel();
    private final HistoricalPurchaseRequisitionTableModel historicalPurchaseRequisitionTableModel = new HistoricalPurchaseRequisitionTableModel();
    private final PendingPurchaseRequisitionTableModel pendingPurchaseRequisitionTableModel = new PendingPurchaseRequisitionTableModel();
    private final PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel(PurchaseOrderTableModel.POStatus.ALL);

    public SalesManagerController(SalesManager salesManagerUser) {
        super(salesManagerUser);
        this.dashboard = new SalesManagerDashboard();
    }

    @Override
    protected JFrame createView() {
        return dashboard;
    }

    @Override
    protected void loadInitialData() {
        loadTables();
        updateTotalSaleAmount();
    }

    @Override
    protected void setupCustomListeners() {
        setupSaleListeners();
        setupItemListeners();
        setupSupplierListeners();
        setupPRListeners();
        setupPOListeners();
    }

    private void loadTables() {
        dashboard.getItemOnSaleTable().setModel(itemOnSaleTableModel);
        dashboard.getItemNotOnSaleTable().setModel(itemNotOnSaleTableModel);
        dashboard.getSalesTable().setModel(saleTableModel);
        dashboard.getItemSaleTable().setModel(itemSaleTableModel);
        dashboard.getSupplierTable().setModel(supplierTableModel);
        dashboard.getPendingRequisitionTable().setModel(pendingPurchaseRequisitionTableModel);
        dashboard.getHistoricalRequisitionTable().setModel(historicalPurchaseRequisitionTableModel);
        dashboard.getOrderTable().setModel(purchaseOrderTableModel);
    }

    private void setupSaleListeners() {
        dashboard.getAddSalesButton().addActionListener(e -> {
            salesService.addSale(dashboard);
            refreshSalesPanel();
            refreshItemPanel();
        });

        dashboard.getSalesTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = dashboard.getSalesTable().getSelectedRow();
                Sales selectedSale = saleTableModel.getSalesAt(row);
                if (selectedSale != null) {
                    salesService.displaySaleDetails(dashboard, selectedSale);
                    refreshSalesPanel();
                    refreshItemPanel();
                }
            }
        });

        setupSearchFieldListener(dashboard.getSalesSearchInput(), saleTableModel::filterByKeyword);
        setupSearchFieldListener(dashboard.getItemSaleSearchInput(), itemSaleTableModel::filterByKeyword);
    }

    private void setupItemListeners() {
        dashboard.getAddItemButton().addActionListener(e -> {
            itemService.addItem(dashboard);
            refreshSalesPanel();
            refreshItemPanel();
        });

        setupSearchFieldListener(dashboard.getItemSearchInput(), itemOnSaleTableModel::filterByKeyword);
        setupSearchFieldListener(dashboard.getItemNotSaleSearchInput(), itemNotOnSaleTableModel::filterByKeyword);
    }

    private void setupSupplierListeners() {
        setupSearchFieldListener(dashboard.getSupplierSearchInput(), supplierTableModel::filterByKeyword);
    }

    private void setupPRListeners() {
        setupSearchFieldListener(dashboard.getRequisitionSearch(), pendingPurchaseRequisitionTableModel::filterByKeyword);
        setupSearchFieldListener(dashboard.getHistoricalRequisitionSearch(), historicalPurchaseRequisitionTableModel::filterByKeyword);
    }

    private void setupPOListeners() {
        setupSearchFieldListener(dashboard.getOrderSearchInput(), purchaseOrderTableModel::filterByKeyword);
    }

    private void setupSearchFieldListener(JTextField textField, java.util.function.Consumer<String> searchFunction) {
        textField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String input = textField.getText().trim();
                    searchFunction.accept(input);
                }
            }
        });
    }

    private void updateTotalSaleAmount() {
        double total = salesService.calculateTodaySalesTotal();
        dashboard.getTotalAmount().setText(Double.toString(total));
    }

    private void refreshSalesPanel() {
        saleTableModel.refresh();
        itemSaleTableModel.refresh();
        updateTotalSaleAmount();
    }

    private void refreshItemPanel() {
        itemOnSaleTableModel.refresh();
        itemNotOnSaleTableModel.refresh();
    }
}
