package controllers;

import com.toedter.calendar.JDateChooser;
import models.users.SalesManager;
import models.Sales;
import services.ItemService;
import services.SalesService;
import tables.*;
import views.SalesManagerDashboard;

import javax.swing.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import models.Item;
import models.PurchaseRequisition;
import models.Supplier;
import services.PurchaseRequisitionService;
import services.SupplierService;
import utils.DateTimeService;
import utils.LowStockRenderer;

public class SalesManagerController extends BaseController {

    private final SalesManagerDashboard dashboard;
    private final SalesService salesService = new SalesService();
    private final ItemService itemService = new ItemService();
    private final SupplierService supplierService = new SupplierService();
    private final PurchaseRequisitionService PRService = new PurchaseRequisitionService();

    // tables
    JTable salesTable;
    JTable itemSaleTable;

    // Table Models
    private final ItemOnSaleTableModel itemOnSaleTableModel = new ItemOnSaleTableModel();
    private final ItemNotOnSaleTableModel itemNotOnSaleTableModel = new ItemNotOnSaleTableModel();
    private final DailySalesTableModel saleTableModel = new DailySalesTableModel();
    private final ItemSaleTableModel itemSaleTableModel = new ItemSaleTableModel();
    private final SupplierTableModel supplierTableModel = new SupplierTableModel();
    private final HistoricalPurchaseRequisitionTableModel historicalPurchaseRequisitionTableModel = new HistoricalPurchaseRequisitionTableModel();
    private final PendingPurchaseRequisitionTableModel pendingPurchaseRequisitionTableModel = new PendingPurchaseRequisitionTableModel();
    private final PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel(PurchaseOrderTableModel.POStatus.ALL);

    private final String today = DateTimeService.getCurrentDate();

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
        updateTotalSaleAmount(today);
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

        LowStockRenderer rowRenderer = new LowStockRenderer(4);
        for (int i = 0; i < dashboard.getItemOnSaleTable().getColumnCount(); i++) {
            dashboard.getItemOnSaleTable().getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
            dashboard.getItemNotOnSaleTable().getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }

        salesTable = dashboard.getSalesTable();
        salesTable.setModel(saleTableModel);

        itemSaleTable = dashboard.getItemSaleTable();
        itemSaleTable.setModel(itemSaleTableModel);

        dashboard.getSupplierTable().setModel(supplierTableModel);
        dashboard.getPendingRequisitionTable().setModel(pendingPurchaseRequisitionTableModel);
        dashboard.getHistoricalRequisitionTable().setModel(historicalPurchaseRequisitionTableModel);
        dashboard.getOrderTable().setModel(purchaseOrderTableModel);
    }

    private void setupSaleListeners() {
        dashboard.getAddSalesButton().addActionListener(e -> {
            Date selectedDate = dashboard.getDateChooser().getDate();
            String formattedDate = selectedDate != null
                    ? new SimpleDateFormat("yyyy-MM-dd").format(selectedDate)
                    : today;

            salesService.addSale(dashboard, formattedDate);

            refreshAll();
        });

        salesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = dashboard.getSalesTable().getSelectedRow();
                Sales selectedSale = saleTableModel.getSalesAt(row);
                if (selectedSale != null) {
                    salesService.displaySaleDetails(dashboard, selectedSale);
                    refreshAll();
                }
            }
        });

        setupSearchFieldListener(dashboard.getSalesSearchInput(), saleTableModel::filterByKeyword);
        setupSearchFieldListener(dashboard.getItemSaleSearchInput(), itemSaleTableModel::filterByKeyword);
    }

    private void setupItemListeners() {
        JDateChooser dateChooser = dashboard.getDateChooser();

        dateChooser.getDateEditor().addPropertyChangeListener("date", evt -> {
            Date selectedDate = dateChooser.getDate();

            if (selectedDate != null) {
                String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate);

                saleTableModel.setDate(formattedDate);
                itemSaleTableModel.setDate(formattedDate);
                updateTotalSaleAmount(formattedDate);
            }
        });

        dashboard.getAddItemButton().addActionListener(e -> {
            itemService.addItem(dashboard);

            refreshAll();
        });

        dashboard.getItemOnSaleTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = dashboard.getItemOnSaleTable().getSelectedRow();
                Item selectedItem = itemOnSaleTableModel.getItemAt(row);
                if (selectedItem != null) {
                    itemService.displayItemDetails(dashboard, selectedItem);

                    refreshAll();
                }
            }
        });

        dashboard.getItemNotOnSaleTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = dashboard.getItemNotOnSaleTable().getSelectedRow();
                Item selectedItem = itemOnSaleTableModel.getItemAt(row);
                if (selectedItem != null) {
                    itemService.displayItemDetails(dashboard, selectedItem);

                    refreshAll();
                }
            }
        });

        setupSearchFieldListener(dashboard.getItemSearchInput(), itemOnSaleTableModel::filterByKeyword);
        setupSearchFieldListener(dashboard.getItemNotSaleSearchInput(), itemNotOnSaleTableModel::filterByKeyword);
    }

    private void setupSupplierListeners() {
        dashboard.getAddSupplierButton().addActionListener(e -> {
            supplierService.addSupplier(dashboard);

            refreshAll();
        });

        dashboard.getSupplierTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = dashboard.getSupplierTable().getSelectedRow();
                Supplier selectedSupplier = supplierTableModel.getSupplierAt(row);
                if (selectedSupplier != null) {
                    supplierService.displaySupplierDetails(dashboard, selectedSupplier);

                    refreshAll();
                }
            }
        });

        setupSearchFieldListener(dashboard.getSupplierSearchInput(), supplierTableModel::filterByKeyword);
    }

    private void setupPRListeners() {
        dashboard.getAddRequisitionButton().addActionListener(e -> {
            PRService.showCreateForm(dashboard, () -> {
            });

            refreshAll();
        });

        dashboard.getPendingRequisitionTable().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = dashboard.getPendingRequisitionTable().getSelectedRow();
                PurchaseRequisition selectedPR = pendingPurchaseRequisitionTableModel.getRequisitionAt(row);

                if (selectedPR != null) {
                    PRService.showActionPanel(selectedPR, dashboard, () -> {
                    });

                    refreshAll();
                }
            }
        });

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

    private void updateTotalSaleAmount(String date) {
        double total = salesService.calculateDailySalesTotal(date);
        dashboard.getTotalAmount().setText(Double.toString(total));
    }

    private void refreshSalesPanel() {
        saleTableModel.refresh();
        itemSaleTableModel.refresh();
        updateTotalSaleAmount(today);
    }

    private void refreshItemPanel() {
        itemOnSaleTableModel.refresh();
        itemNotOnSaleTableModel.refresh();
    }

    private void refreshSupplierPanel() {
        supplierTableModel.refresh();
    }

    private void refreshPRPanel() {
        historicalPurchaseRequisitionTableModel.refresh();
        pendingPurchaseRequisitionTableModel.refresh();
    }

    private void refreshPOPanel() {
        purchaseOrderTableModel.refresh();
    }

    private void refreshAll() {
        refreshSalesPanel();
        refreshItemPanel();
        refreshSupplierPanel();
        refreshPRPanel();
        refreshPOPanel();
    }
}
