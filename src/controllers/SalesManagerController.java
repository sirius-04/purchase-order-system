package controllers;

import models.users.SalesManager;
import views.SalesManagerDashboard;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import models.Sales;
import services.ItemService;
import services.SalesService;
import tables.DailySalesTableModel;
import tables.ItemNotOnSaleTableModel;
import tables.ItemOnSaleTableModel;
import tables.ItemSaleTableModel;
import tables.SupplierTableModel;
import tables.HistoricalPurchaseRequisitionTableModel;
import tables.PendingPurchaseRequisitionTableModel;
import tables.PurchaseOrderTableModel;

public class SalesManagerController extends BaseController {

    private SalesManagerDashboard dashboard;
    private SalesService salesService = new SalesService();
    private ItemService itemService = new ItemService();

    // table models
    ItemOnSaleTableModel itemOnSaleTableModel = new ItemOnSaleTableModel();
    ItemNotOnSaleTableModel itemNotOnSaleTableModel = new ItemNotOnSaleTableModel();
    DailySalesTableModel saleTableModel = new DailySalesTableModel();
    ItemSaleTableModel itemSaleTableModel = new ItemSaleTableModel();
    SupplierTableModel supplierTableModel = new SupplierTableModel();
    HistoricalPurchaseRequisitionTableModel historicalPurchaseRequisitionTableModel = new HistoricalPurchaseRequisitionTableModel();
    PendingPurchaseRequisitionTableModel pendingPurchaseRequisitionTableModel = new PendingPurchaseRequisitionTableModel();
    PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel(PurchaseOrderTableModel.POStatus.ALL);

    // tables
    private JTable itemOnSaleTable;
    private JTable itemNotOnSaleTable;
    private JTable dailySalesTable;
    private JTable itemSaleTable;
    private JTable purchaseOrderTable;
    private JTable historicalPurchaseRequisitionTable;
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
        
        // Search - Sales Today
        JTextField saleSearchInput = dashboard.getSalesSearchInput();
        saleSearchInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = saleSearchInput.getText();
                    saleTableModel.filterByKeyword(inputText);
                    
                    saleSearchInput.setText("");
                }
            }
        });
        
        // Search - Sales of Items
        JTextField itemSaleSearchInput = dashboard.getItemSaleSearchInput();
        itemSaleSearchInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = itemSaleSearchInput.getText();
                    itemSaleTableModel.filterByKeyword(inputText);
                    
                    itemSaleSearchInput.setText("");
                }
            }
        });
        // Search - Item On Sale
        JTextField itemOnSaleSaerchInput = dashboard.getItemSearchInput();
        itemOnSaleSaerchInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = itemOnSaleSaerchInput.getText();
                    itemOnSaleTableModel.filterByKeyword(inputText);
                    
                    itemOnSaleSaerchInput.setText("");
                }
            }
        });
        
        // Search - Item not on Sale
        JTextField itemNotOnSaleSearchInput = dashboard.getItemNotSaleSearchInput();
        itemNotOnSaleSearchInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = itemNotOnSaleSearchInput.getText();
                    itemNotOnSaleTableModel.filterByKeyword(inputText);
                    
                    itemNotOnSaleSearchInput.setText("");
                }
            }
        });
        
        // Search - Supplier
        JTextField supplierSearchInput = dashboard.getSupplierSearchInput();
        supplierSearchInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = supplierSearchInput.getText();
                    supplierTableModel.filterByKeyword(inputText);
                    
                    supplierSearchInput.setText("");
                }
            }
        });
        
         // Search - Pending Purchase Requisitions
        JTextField pendingPRSearchInput = dashboard.getRequisitionSearch();
        pendingPRSearchInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = pendingPRSearchInput.getText();
                    pendingPurchaseRequisitionTableModel.filterByKeyword(inputText);
                    
                    pendingPRSearchInput.setText("");
                }
            }
        });
        
        // Search - Historical Purchase Requisitions
        JTextField historicalPRSearchInput = dashboard.getHistoricalRequisitionSearch();
        historicalPRSearchInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = historicalPRSearchInput.getText();
                    historicalPurchaseRequisitionTableModel.filterByKeyword(inputText);
                    
                    historicalPRSearchInput.setText("");
                }
            }
        });
        
        // Search - Purchase Order
        JTextField purchaseOrderSearchInput = dashboard.getOrderSearchInput();
        purchaseOrderSearchInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = purchaseOrderSearchInput.getText();
                    purchaseOrderTableModel.filterByKeyword(inputText);
                    
                    purchaseOrderSearchInput.setText("");
                }
            }
        });
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
        
        // supplier table
        supplierTable = dashboard.getSupplierTable();
        supplierTable.setModel(supplierTableModel);
        
         // pending purchase requisition table
        pendingRequisitionTable = dashboard.getPendingRequisitionTable();
        pendingRequisitionTable.setModel(pendingPurchaseRequisitionTableModel);
        
        // historical purchase requisition table
        historicalPurchaseRequisitionTable = dashboard.getHistoricalRequisitionTable();
        historicalPurchaseRequisitionTable.setModel(historicalPurchaseRequisitionTableModel);
        
        // purchase order  table
        purchaseOrderTable = dashboard.getOrderTable();
        purchaseOrderTable.setModel(purchaseOrderTableModel);
        
       
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
}