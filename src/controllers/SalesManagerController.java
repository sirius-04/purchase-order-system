/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import helpers.BaseTableHelper;
import helpers.DailySalesTableHelper;
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
    
    //  Daily Sales
    private JTable dailySalesTable;
    
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
        dailySalesTable = dashboard.getSalesTable();
        DefaultTableModel model = (DefaultTableModel) dailySalesTable.getModel();
        JLabel totalAmountText = dashboard.getTotalAmount();

        DailySalesTableHelper.populateTodaySales(model);

        totalAmountText.setText(DailySalesTableHelper.calculateColumnTotal(dailySalesTable, 6));
        
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
