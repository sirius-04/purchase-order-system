/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import models.InventoryUpdate;
import models.Payment;
import models.users.FinanceManager;
import services.PurchaseOrderService;
import services.ReportService;
import tables.HistoricalPurchaseRequisitionTableModel;
import tables.InventoryUpdateTableModel;
import tables.PaymentTableModel;
import tables.PendingPurchaseRequisitionTableModel;
import tables.PurchaseOrderTableModel;
import views.FinanceManagerDashboard;
import models.PurchaseOrder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import services.InventoryUpdateService;
import services.PDFExportService;
import services.PaymentService;
import services.UserService;

/**
 *
 * @author Chan Yong Liang
 */
public class FinanceManagerController extends BaseController {

    private FinanceManagerDashboard dashboard;
    private PurchaseOrderService poService = new PurchaseOrderService();
    private PaymentService paymentService = new PaymentService();
    private InventoryUpdateService inventoryUpdateService = new InventoryUpdateService();
    private PDFExportService pdfExportService = new PDFExportService();
    private UserService userService = new UserService();

    // table models
    PaymentTableModel paymentTableModel = new PaymentTableModel();
    PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel(PurchaseOrderTableModel.POStatus.PENDING);
    PendingPurchaseRequisitionTableModel pendingRequisitionTableModel = new PendingPurchaseRequisitionTableModel();
    HistoricalPurchaseRequisitionTableModel historicalRequisitionTableModel = new HistoricalPurchaseRequisitionTableModel();
    InventoryUpdateTableModel inventoryTableModel = new InventoryUpdateTableModel();
    
    private JTable purchaseOrderTable;
    private JTable inventoryTable;
    private JTable paymentTable;
    private JTable historicalRequisitionTable;
    private JTable pendingRequisitionTable;
    private JFreeChart dailyProfitChart;
    
    public FinanceManagerController(FinanceManager user) {
        super(user);
    }

    @Override
    protected JFrame createView() {
        dashboard = new FinanceManagerDashboard();
        return dashboard;
    }

    @Override
    protected void loadInitialData() {
        loadTables();
        setupMonthComboBox();
        showDailyProfitChart();
    }

    @Override
    protected void setupCustomListeners() {
        approvePOListener();
        verifyUpdateListener();
        processPaymentListener();
        exportListeners();
        logOut();
        
        // Item Table - PO
        JTextField poSearch = dashboard.getOrderSearchInput();
        poSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = poSearch.getText();
                    purchaseOrderTableModel.filterByKeyword(inputText);
                    
                    poSearch.setText((""));
                }
            }
        });
        
        // Item Table - Inventory
        JTextField inventorySearch = dashboard.getInventorySearchInput();
        inventorySearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = inventorySearch.getText();
                    inventoryTableModel.filterByKeyword(inputText);
                    
                    inventorySearch.setText((""));
                }
            }
        });
        
        // Item Table - pending PR
        JTextField pendingPRSearch = dashboard.getRequisitionSearchInput();
        pendingPRSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = pendingPRSearch.getText();
                    pendingRequisitionTableModel.filterByKeyword(inputText);
                    
                    pendingPRSearch.setText((""));
                }
            }
        });
        
        // Item Table - historical PR
        JTextField historicalPRSearch = dashboard.getHistoricalRequisitionSearchInput();
        historicalPRSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = historicalPRSearch.getText();
                    historicalRequisitionTableModel.filterByKeyword(inputText);
                    
                    historicalPRSearch.setText((""));
                }
            }
        });
    }

    private void loadTables() {
        // po table
        purchaseOrderTable = dashboard.getOrderTable();
        purchaseOrderTable.setModel(purchaseOrderTableModel);

        // payment table
        paymentTable = dashboard.getPaymentTable();
        paymentTable.setModel(paymentTableModel);

        // pr tables
        pendingRequisitionTable = dashboard.getPendingRequisitionTable();
        pendingRequisitionTable.setModel(pendingRequisitionTableModel);
        historicalRequisitionTable = dashboard.getHistoricalRequisitionTable();
        historicalRequisitionTable.setModel(historicalRequisitionTableModel);
        
        // inventory table
        inventoryTable = dashboard.getInventoryTable();
        inventoryTable.setModel(inventoryTableModel);
    }
    
    private void approvePOListener() {
       purchaseOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = purchaseOrderTable.getSelectedRow();
                if (row != -1) {
                   PurchaseOrderTableModel purchaseOrderModel = (PurchaseOrderTableModel) purchaseOrderTable.getModel();
                   InventoryUpdateTableModel inventoryModel = (InventoryUpdateTableModel) inventoryTable.getModel();
                   PurchaseOrder selectedPO = purchaseOrderModel.getPurchaseOrderAt(row);
                   
                   poService.addApprovalListener(dashboard, selectedPO);
                   purchaseOrderModel.refresh();
                   inventoryModel.refresh();
                }
            }
        });
    }
    
    private void verifyUpdateListener() {
        inventoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = inventoryTable.getSelectedRow();
                if (row != -1) {
                    InventoryUpdateTableModel inventoryModel = (InventoryUpdateTableModel) inventoryTable.getModel();
                    PaymentTableModel paymentModel = (PaymentTableModel) paymentTable.getModel();
                    InventoryUpdate inventory = inventoryModel.getInventoryUpdateAt(row);

                    inventoryUpdateService.verifyUpdate(dashboard, inventory);
                    inventoryModel.refresh();
                    paymentModel.refresh();
                    }
                }
            }); 
    } 
    
    private void processPaymentListener() {
        paymentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = paymentTable.getSelectedRow();
                if (row != -1) {
                    PaymentTableModel paymentModel = (PaymentTableModel) paymentTable.getModel();
                    Payment selectedPayment = paymentModel.getPaymentAt(row);
                    
                paymentService.processPayment(dashboard, selectedPayment);
                paymentModel.refresh();
                }
            }
        });
    }
    
   
    private void setupMonthComboBox() {
        JComboBox<String> monthCombo = (JComboBox<String>) dashboard.getMonthButton();
        monthCombo.removeAllItems();

        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            monthCombo.addItem(month);
        }

        String currentMonth = YearMonth.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        monthCombo.setSelectedItem(currentMonth);

        // Add listener to refresh chart based on month
        monthCombo.addActionListener(e -> showDailyProfitChart());
    }
    
    private void showDailyProfitChart() {
        ReportService reportService = new ReportService();

        String selectedMonthName = (String) dashboard.getMonthButton().getSelectedItem();
        int year = Year.now().getValue();
        int month = Month.valueOf(selectedMonthName.toUpperCase()).getValue();
        YearMonth selectedYearMonth = YearMonth.of(year, month);

        Map<String, Double> dailyProfitMap = reportService.getDailyProfit(selectedYearMonth);
        
        JPanel chartContainer = dashboard.getChartPanel();
        
        chartContainer.removeAll();
        chartContainer.setLayout(new BorderLayout());

      
        dailyProfitChart = reportService.createDailyProfitChart(dailyProfitMap, selectedYearMonth);

        ChartPanel chartPanel = new ChartPanel(dailyProfitChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(
            chartContainer.getWidth(),
            chartContainer.getHeight()
        ));

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(chartContainer.getBackground());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        wrapperPanel.add(chartPanel, gbc);
        chartContainer.add(wrapperPanel, BorderLayout.CENTER);
        
        chartContainer.revalidate();
        chartContainer.repaint();
    }
    
    private void exportListeners() {
    dashboard.getExportButton().addActionListener(e -> {
        if (dailyProfitChart != null) {
            String selectedMonth = (String) dashboard.getMonthButton().getSelectedItem();
            
            pdfExportService.exportChartToPDF(
                dailyProfitChart, 
                "Daily_Profit_Report_" + selectedMonth
            );
        } else {
            JOptionPane.showMessageDialog(dashboard, "No daily profit chart available to export");
            }
        });
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
 

        
