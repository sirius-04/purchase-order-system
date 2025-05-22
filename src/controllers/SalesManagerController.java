/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import helpers.DailySalesTableHelper;
import helpers.ItemTableHelper;
import helpers.PurchaseOrderTableHelper;
import helpers.SupplierTableHelper;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import models.users.SalesManager;
import views.SalesManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class SalesManagerController extends BaseController {

    private SalesManagerDashboard dashboard;
    
    // item tables
    private JTable itemOnSaleTable;
    private JTable itemNotOnSaleTable;
    
    //  Daily Sales
    private JTable dailySalesTable;
    
    // Purchase Order
    private JTable purchaseOrderTable;
    
    //Suppliers
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
        itemOnSaleTable = dashboard.getItemOnSaleTable();
        itemNotOnSaleTable = dashboard.getItemNotOnSaleTable();
        
        // item on sale table population
        DefaultTableModel itemOnSaleTableModel = (DefaultTableModel) itemOnSaleTable.getModel();
        ItemTableHelper.populateItemOnSale(itemOnSaleTableModel);

        // item not on sale table population
        DefaultTableModel itemNotOnSaleTableModel = (DefaultTableModel) itemNotOnSaleTable.getModel();
        ItemTableHelper.populateItemNotOnSale(itemNotOnSaleTableModel);
        
        dailySalesTable = dashboard.getSalesTable();
        DefaultTableModel model = (DefaultTableModel) dailySalesTable.getModel();
        DailySalesTableHelper.populateTodaySales(model);

        purchaseOrderTable = dashboard.getOrderTable();
        DefaultTableModel orderModel = (DefaultTableModel) purchaseOrderTable.getModel();
        PurchaseOrderTableHelper.populatePurchaseOrder(orderModel);

        JLabel totalAmountText = dashboard.getTotalAmount();
        totalAmountText.setText(DailySalesTableHelper.calculateColumnTotal(dailySalesTable, 6));
//        totalAmountText.setText(PurchaseOrderTableHelper.calculateColumnTotal(purchaseOrderTable, 7));
        
        // Supplier
        supplierTable = dashboard.getSupplierTable();
        DefaultTableModel supplierModel = (DefaultTableModel) supplierTable.getModel();
        SupplierTableHelper.populateSupplier(supplierModel);
        
    }

    @Override
    protected void setupCustomListeners() {
        JButton addSalesButton = dashboard.getAddSalesButton();

        addSalesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }
}
