/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import models.users.InventoryManager;
import views.InventoryManagerDashboard;
import tables.ItemTableModel;
import tables.PurchaseOrderTableModel;
import utils.LowStockRenderer;
import models.PurchaseOrder;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import services.PDFExportService;
import services.ReportService;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryManagerController extends BaseController {
    private InventoryManagerDashboard dashboard;
    
     // table models
    ItemTableModel itemTableModel = new ItemTableModel();
    PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel(PurchaseOrder.Status.fulfilled, false);
    private PDFExportService pdfExportService = new PDFExportService();
    
    private JFreeChart stockReportChart;
    // tables
    private JTable itemTable;
    private JTable purchaseOrderTable;
    
    public InventoryManagerController(InventoryManager user) {
        super(user);
    }

    @Override
    protected JFrame createView() {
        dashboard = new InventoryManagerDashboard();
        return dashboard;
    }

    @Override
    protected void loadInitialData() {
        loadTables();
        showStockReportChart();
    }

    @Override
    protected void setupCustomListeners() {
    
        // Status selection
        JComboBox<String> statusComboBox = dashboard.getStatusComboBox();
        statusComboBox.addActionListener(e -> {
            String selected = (String) statusComboBox.getSelectedItem();

            switch (selected) {
                case "On Sale":
                    itemTableModel.setStatus(ItemTableModel.Status.ON_SALE);
                    break;
                case "Not On Sale":
                    itemTableModel.setStatus(ItemTableModel.Status.NOT_ON_SALE);
                    break;
                default:
                    itemTableModel.setStatus(ItemTableModel.Status.ALL);
                    break;
            }
        });

        // Search input
        JTextField searchInput = dashboard.getItemSearchInput();
        searchInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String inputText = searchInput.getText();
                    itemTableModel.searchByName(inputText);
                }
            }
        });
        
        // stock report
        setupExportListeners();
    }
    
    private void loadTables() {
        // item table
        itemTable = dashboard.getItemTable();
        itemTable.setModel(itemTableModel);
        
        // Render low stock items
        LowStockRenderer rowRenderer = new LowStockRenderer(3);
        for (int i = 0; i < itemTable.getColumnCount(); i++) {
            itemTable.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }
        
        // po table
        purchaseOrderTable = dashboard.getOrderTable();
        purchaseOrderTable.setModel(purchaseOrderTableModel);
        
    }
    
    private void showStockReportChart() {
        ReportService reportService = new ReportService();
        Map<String, Double> quantityMap = reportService.getStockQuantities();
        Map<String, Double> priceMap = reportService.getItemPrices();
        
        stockReportChart = reportService.createStockReportChart(quantityMap, priceMap);
        JFreeChart chart = reportService.createStockReportChart(quantityMap, priceMap);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(
            dashboard.getStockReportPanel().getWidth() - 20, 
            dashboard.getStockReportPanel().getHeight() - 20
        ));
        
        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(dashboard.getStockReportPanel().getBackground());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        wrapperPanel.add(chartPanel, gbc);

        dashboard.getStockReportPanel().setLayout(new BorderLayout());
        dashboard.getStockReportPanel().add(wrapperPanel, BorderLayout.CENTER);
        dashboard.getStockReportPanel().revalidate();
        dashboard.getStockReportPanel().repaint();
    }
    
    private void setupExportListeners() {
        dashboard.getExportButton().addActionListener(e -> {
            if (stockReportChart != null) {
                pdfExportService.exportChartToPDF(stockReportChart, "Stock Report");
            } else {
                JOptionPane.showMessageDialog(dashboard, "No stock report chart available to export");
            }
        });
    }
     
    
}
