/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.Month;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import models.Supplier;
import models.users.Admin;
import models.users.User;
import models.users.UserRole;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import services.ItemService;
import services.PDFExportService;
import services.PurchaseOrderService;
import services.ReportService;
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
    private PDFExportService pdfExportService = new PDFExportService();
    
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
    private JFreeChart dailyProfitChart;
    private JFreeChart stockReportChart;
    private JFreeChart currentChart;
    
    
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
        setupMonthComboBox();
        setupChartComboBox();
    }

    @Override
    protected void setupCustomListeners() {
        registerNewUser();
        handleUserEditAndDelete();
        addItemButtonListener();
        setupSupplierListeners();
        setupGeneratePOListener();
        setupPOClickListener();
        exportListeners();
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
    
    
    private void handleUserEditAndDelete() {
    userTable.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = userTable.getSelectedRow();
            if (row != -1) {
                UserTableModel userModel = (UserTableModel) userTable.getModel();
                User user = userModel.getUserAt(row);

                String[] options = {"Edit User", "Delete User", "Cancel"};
                int choice = javax.swing.JOptionPane.showOptionDialog(
                        dashboard,
                        "What would you like to do with user '" + user.getUsername() + "'?",
                        "User Options",
                        javax.swing.JOptionPane.DEFAULT_OPTION,
                        javax.swing.JOptionPane.PLAIN_MESSAGE,
                        null,
                        options,
                        options[0]
                );

                if (choice == 0) { 
                    userService.renameUserOrChangePassword(dashboard, user);
                    userModel.refresh();
                } else if (choice == 1) { 
                    int confirm = javax.swing.JOptionPane.showConfirmDialog(
                            dashboard,
                            "Are you sure you want to delete user '" + user.getUsername() + "'?",
                            "Confirm Delete",
                            javax.swing.JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                        userService.deleteUser(dashboard, user);
                        userModel.refresh();
                        javax.swing.JOptionPane.showMessageDialog(dashboard, "User deleted successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    }
                }
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
   
    private void setupGeneratePOListener() {
        PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
        Admin currentAdmin = (Admin) currentUser;

        purchaseOrderService.setupGeneratePOListener(
            dashboard.getPendingPRTable(),
            currentAdmin,
            () -> {
                purchaseOrderTableModel.refresh();
                historicalRequisitionTableModel.refresh();
                pendingRequisitionTableModel.refresh();
            }
        );
    }
    
    private void setupPOClickListener() {
        PurchaseOrderService purchaseOrderService = new PurchaseOrderService();
        Admin currentAdmin = (Admin) currentUser;

        purchaseOrderService.setupPOTableClickListener(
            purchaseOrderTable,
            currentAdmin,
            () -> purchaseOrderTableModel.refresh()
        );
    } 
    
    private void setupMonthComboBox() {
        JComboBox<String> monthCombo = (JComboBox<String>) dashboard.getMonthComboBox();
        monthCombo.removeAllItems();

        String[] months = {"January", "February", "March", "April", "May", "June", 
                          "July", "August", "September", "October", "November", "December"};
        for (String month : months) {
            monthCombo.addItem(month);
        }

        String currentMonth = YearMonth.now().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        monthCombo.setSelectedItem(currentMonth);

        monthCombo.addActionListener(e -> showDailyProfitChart());
    }
    
    private void setupChartComboBox() {
        JComboBox<String> chartCombo = (JComboBox<String>) dashboard.getChartComboBox();
        chartCombo.removeAllItems();
        
        String[] charts = {"Stock Chart", "Finance Chart"};
        for (String chart: charts) {
            chartCombo.addItem(chart);
        }
        chartCombo.setSelectedItem("Finance Chart");
        
        chartCombo.addActionListener(e -> {
            String selectedChart = (String) chartCombo.getSelectedItem();
            if ("Stock Chart".equals(selectedChart)) {
                showStockReportChart();
            } else if ("Finance Chart".equals(selectedChart)) {
                showDailyProfitChart();
            }
        });
    }
    
    private void showDailyProfitChart() {
        ReportService reportService = new ReportService();

        String selectedMonthName = (String) dashboard.getMonthComboBox().getSelectedItem();
        int year = Year.now().getValue();
        int month = Month.valueOf(selectedMonthName.toUpperCase()).getValue();
        YearMonth selectedYearMonth = YearMonth.of(year, month);

        Map<String, Double> dailyProfitMap = reportService.getDailyProfit(selectedYearMonth);
        
        JPanel chartContainer = dashboard.getChartPanel();
        
        chartContainer.removeAll();
        chartContainer.setLayout(new BorderLayout());

      
        dailyProfitChart = reportService.createDailyProfitChart(dailyProfitMap, selectedYearMonth);
        currentChart = dailyProfitChart;

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
    
    private void showStockReportChart() {
        ReportService reportService = new ReportService();
        Map<String, Double> quantityMap = reportService.getStockQuantities();
        Map<String, Double> priceMap = reportService.getItemPrices();
        
        JPanel stockChartContainer = dashboard.getChartPanel();
        
        stockReportChart = reportService.createStockReportChart(quantityMap, priceMap);
        currentChart = stockReportChart;
        
        stockChartContainer.removeAll();
        stockChartContainer.setLayout(new BorderLayout());

        ChartPanel chartPanel = new ChartPanel(stockReportChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(
            stockChartContainer.getWidth(), 
            stockChartContainer.getHeight()
        ));
        
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(stockChartContainer.getBackground());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        wrapperPanel.add(chartPanel, gbc);

        stockChartContainer.setLayout(new BorderLayout());
        stockChartContainer.add(wrapperPanel, BorderLayout.CENTER);
        stockChartContainer.revalidate();
        stockChartContainer.repaint();
    }
    
    private void exportListeners() {
        dashboard.getExportButton().addActionListener(e -> {
            if (currentChart != null) {
                String selectedChart = (String) dashboard.getChartComboBox().getSelectedItem();
                String filename = "Chart_Report_" + selectedChart.replace(" ", "_");
                pdfExportService.exportChartToPDF(currentChart, filename);
            } else {
                JOptionPane.showMessageDialog(dashboard, "No chart available to export");
            }
        });
    }
}
