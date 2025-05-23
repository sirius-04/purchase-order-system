package controllers;

import helpers.*;
import models.Item;
import models.Sales;
import models.users.SalesManager;
import repository.ItemRepository;
import repository.SalesRepository;
import services.DateTimeService;
import services.IdGenerator;
import views.SalesManagerDashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SalesManagerController extends BaseController {

    private SalesManagerDashboard dashboard;
    private IdGenerator idGenerator = new IdGenerator();

    // Tables
    private JTable itemOnSaleTable;
    private JTable itemNotOnSaleTable;
    private JTable dailySalesTable;
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
        populateAllTables();
        updateTotalSaleAmount();
    }

    @Override
    protected void setupCustomListeners() {
        setupAddSaleListener();
    }

    private void populateAllTables() {
        populateItemTables();
        populateSalesTable();
        populatePurchaseOrderTable();
        populateRequisitionTables();
        populateSupplierTable();
    }

    private void populateItemTables() {
        itemOnSaleTable = dashboard.getItemOnSaleTable();
        itemNotOnSaleTable = dashboard.getItemNotOnSaleTable();
        ItemTableHelper.populateItemOnSale((DefaultTableModel) itemOnSaleTable.getModel());
        ItemTableHelper.populateItemNotOnSale((DefaultTableModel) itemNotOnSaleTable.getModel());
    }

    private void populateSalesTable() {
        dailySalesTable = dashboard.getSalesTable();
        DailySalesTableHelper.populateTodaySales((DefaultTableModel) dailySalesTable.getModel());
    }

    private void populatePurchaseOrderTable() {
        purchaseOrderTable = dashboard.getOrderTable();
        PurchaseOrderTableHelper.populatePurchaseOrder((DefaultTableModel) purchaseOrderTable.getModel());
    }

    private void populateRequisitionTables() {
        historicalRequisitionTable = dashboard.getHistoricalRequisitionTable();
        pendingRequisitionTable = dashboard.getPendingRequisitionTable();
        DefaultTableModel pendingModel = (DefaultTableModel) pendingRequisitionTable.getModel();
        DefaultTableModel historicalModel = (DefaultTableModel) historicalRequisitionTable.getModel();
        PurchaseRequisitionsTableHelper.populateAllRequisitions(pendingModel, historicalModel);
    }

    private void populateSupplierTable() {
        supplierTable = dashboard.getSupplierTable();
        SupplierTableHelper.populateSupplier((DefaultTableModel) supplierTable.getModel());
    }

    private void updateTotalSaleAmount() {
        JLabel totalAmountText = dashboard.getTotalAmount();
        totalAmountText.setText(DailySalesTableHelper.calculateColumnTotal(dailySalesTable, 6));
    }

    private void setupAddSaleListener() {
        JButton addSalesButton = dashboard.getAddSalesButton();
        addSalesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showAddSaleDialog();
            }
        });
    }

    private void showAddSaleDialog() {
        List<Item> itemOnSaleList = new ItemRepository().getAll().stream()
                .filter(item -> item.getStatus() == Item.Status.onSale)
                .toList();

        JComboBox<Item> comboBox = new JComboBox<>(itemOnSaleList.toArray(new Item[0]));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        JPanel panel = createAddSalePanel(comboBox, quantitySpinner);

        int result = JOptionPane.showConfirmDialog(null, panel, "Add Sale", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            processAddSale((Item) comboBox.getSelectedItem(), (Integer) quantitySpinner.getValue());
        }
    }

    private JPanel createAddSalePanel(JComboBox<Item> comboBox, JSpinner spinner) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Select Item:"));
        panel.add(comboBox);
        panel.add(new JLabel("Select Quantity:"));
        panel.add(spinner);
        return panel;
    }

    private void processAddSale(Item selectedItem, int quantity) {
        String generatedSaleId = idGenerator.generateNewId(Sales.class);
        double totalAmount = selectedItem.getSellPrice() * quantity;
        String currentDate = DateTimeService.getCurrentDate();
        String currentTime = DateTimeService.getCurrentTime();

        Sales newSale = new Sales(generatedSaleId, selectedItem.getItemId(), quantity, currentDate, currentTime, totalAmount);
        new SalesRepository().save(newSale);

        JOptionPane.showMessageDialog(null, "Sale recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        DailySalesTableHelper.populateTodaySales((DefaultTableModel) dailySalesTable.getModel());
        updateTotalSaleAmount();
    }
}
