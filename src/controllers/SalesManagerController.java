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
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import models.Item;
import models.Sales;
import models.users.SalesManager;
import repository.ItemRepository;
import repository.SalesRepository;
import services.DateTimeService;
import services.IdGenerator;
import views.SalesManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class SalesManagerController extends BaseController {

    private SalesManagerDashboard dashboard;
    private IdGenerator idGenerator = new IdGenerator();

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
        populateTables();
        setSaleTotal();

    }

    @Override
    protected void setupCustomListeners() {
        addSale();
    }

    private void addSale() {
        JButton addSalesButton = dashboard.getAddSalesButton();

        addSalesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ItemRepository itemRepo = new ItemRepository();
                List<Item> itemOnSaleList = itemRepo.getAll()
                        .stream()
                        .filter(item -> item.getStatus() == Item.Status.onSale)
                        .toList();

                JComboBox<Item> comboBox = new JComboBox<>(itemOnSaleList.toArray(new Item[0]));

                JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

                JPanel panel = addSaleDialogPanel(comboBox, quantitySpinner);

                int result = JOptionPane.showConfirmDialog(null, panel, "Add Sale", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    Item selectedItem = (Item) comboBox.getSelectedItem();
                    int quantity = (Integer) quantitySpinner.getValue();

                    String generatedSaleId = idGenerator.generateNewId(Sales.class);
                    double totalAmount = selectedItem.getSellPrice() * quantity;

                    String currentDate = DateTimeService.getCurrentDate();
                    String currentTime = DateTimeService.getCurrentTime();

                    Sales newSale = new Sales(generatedSaleId, selectedItem.getItemId(), quantity, currentDate, currentTime, totalAmount);

                    SalesRepository salesRepo = new SalesRepository();
                    salesRepo.save(newSale);

                    JOptionPane.showMessageDialog(null, "Sale recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

                    dailySalesTable = dashboard.getSalesTable();
                    DefaultTableModel dailySaleModel = (DefaultTableModel) dailySalesTable.getModel();
                    DailySalesTableHelper.populateTodaySales(dailySaleModel);
                    setSaleTotal();

                }

            }
        });
    }

    private void populateTables() {
        itemOnSaleTable = dashboard.getItemOnSaleTable();
        itemNotOnSaleTable = dashboard.getItemNotOnSaleTable();

        // item on sale table population
        DefaultTableModel itemOnSaleTableModel = (DefaultTableModel) itemOnSaleTable.getModel();
        ItemTableHelper.populateItemOnSale(itemOnSaleTableModel);

        // item not on sale table population
        DefaultTableModel itemNotOnSaleTableModel = (DefaultTableModel) itemNotOnSaleTable.getModel();
        ItemTableHelper.populateItemNotOnSale(itemNotOnSaleTableModel);

        dailySalesTable = dashboard.getSalesTable();
        DefaultTableModel dailySaleModel = (DefaultTableModel) dailySalesTable.getModel();
        DailySalesTableHelper.populateTodaySales(dailySaleModel);

        purchaseOrderTable = dashboard.getOrderTable();
        DefaultTableModel orderModel = (DefaultTableModel) purchaseOrderTable.getModel();
        PurchaseOrderTableHelper.populatePurchaseOrder(orderModel);

        // Supplier
        supplierTable = dashboard.getSupplierTable();
        DefaultTableModel supplierModel = (DefaultTableModel) supplierTable.getModel();
        SupplierTableHelper.populateSupplier(supplierModel);
    }

    private void setSaleTotal() {
        JLabel totalAmountText = dashboard.getTotalAmount();
        totalAmountText.setText(DailySalesTableHelper.calculateColumnTotal(dailySalesTable, 6));
    }

    private JPanel addSaleDialogPanel(JComboBox<Item> comboBox, JSpinner spinner) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Select Item:"));
        panel.add(comboBox);
        panel.add(new JLabel("Select Quantity:"));
        panel.add(spinner);
        return panel;
    }

}
