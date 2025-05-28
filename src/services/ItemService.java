/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import models.Supplier;
import repository.ItemSupplierRepository;
import repository.SupplierRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemService {

    private ItemSupplierRepository itemSupplierRepo = new ItemSupplierRepository();
    private SupplierRepository supplierRepo = new SupplierRepository();

    public void addItem(Component parent) {
        JTextField nameField = new JTextField(20);
        JSpinner stockSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));
        JTextField priceField = new JTextField();
        JTextField sellPriceField = new JTextField();

        JPanel itemPanel = new JPanel();
        itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.Y_AXIS));
        itemPanel.add(new JLabel("Enter item details:"));
        itemPanel.add(new JLabel("Name:"));
        itemPanel.add(nameField);
        itemPanel.add(new JLabel("Stock Quantity:"));
        itemPanel.add(stockSpinner);
        itemPanel.add(new JLabel("Price (cost):"));
        itemPanel.add(priceField);
        itemPanel.add(new JLabel("Selling Price:"));
        itemPanel.add(sellPriceField);

        Object[] itemOptions = {"Next", "Cancel"};
        int itemResult = JOptionPane.showOptionDialog(
                parent,
                itemPanel,
                "New Item",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                itemOptions,
                itemOptions[0]
        );

        if (itemResult == 0) {
            String[] supplierOptions = getSupplierOptions();
            JComboBox<String> supplierCombo = new JComboBox<>(supplierOptions);

            JPanel supplierPanel = new JPanel();
            supplierPanel.setLayout(new BoxLayout(supplierPanel, BoxLayout.Y_AXIS));
            supplierPanel.add(new JLabel("Select Supplier ID:"));
            supplierPanel.add(supplierCombo);

            Object[] supplierPanelOptions = {"Confirm", "Add New Supplier", "Cancel"};
            int supplierPanelResult = JOptionPane.showOptionDialog(
                    parent,
                    supplierPanel,
                    "Select Supplier",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    supplierPanelOptions,
                    supplierPanelOptions[0]
            );

            if (supplierPanelResult == 0) {
                // add new item
            } else if (supplierPanelResult == 1) {
                addNewSupplier();
                // add new item
            }
        }
    }

    private String[] getSupplierOptions() {
        List<Supplier> supplierList = supplierRepo.getAll();
        String[] supplierOptions = new String[supplierList.size()];

        for (int i = 0; i < supplierList.size(); i++) {
            supplierOptions[i] = supplierList.get(i).getSupplierId();
        }

        return supplierOptions;
    }

    private void addNewSupplier() {
        System.out.println("add new supplier");
    }
}
