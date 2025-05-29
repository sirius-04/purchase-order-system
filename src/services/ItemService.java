/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import models.Supplier;
import repository.SupplierRepository;
import models.Item;
import repository.ItemRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemService {

    private ItemRepository itemSupplierRepo = new ItemRepository();
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
    
    public void editItem(Component parent, Item item) {
        if (item == null) return;
        
        // Jpanel's Input
        JTextField nameField = new JTextField(item.getName(), 20);
        JSpinner stockSpinner = new JSpinner(new SpinnerNumberModel(item.getStockQuantity(), 0, 10000, 1));
        JTextField priceField = new JTextField(String.valueOf(item.getPrice()));
        JComboBox<Item.Status> statusCombo = new JComboBox<>(Item.Status.values());
        // onSale -> On Sale, notOnSale -> No On Sale
        statusCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Item.Status) {
                    Item.Status status = (Item.Status) value;
                    setText(status == Item.Status.onSale ? "On Sale" : "Not On Sale");
                }
                return this;
            }
        });

        // JPanel UI start here
       JPanel editPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        editPanel.add(new JLabel("Edit Item Details:"), gbc);
        gbc.gridwidth = 1;

        // Row 1: Name
        gbc.gridy++;
        editPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        editPanel.add(nameField, gbc);

        // Row 2: Stock Quantity
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Stock Quantity:"), gbc);
        gbc.gridx = 1;
        editPanel.add(stockSpinner, gbc);

        // Row 3: Price
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Price (cost):"), gbc);
        gbc.gridx = 1;
        editPanel.add(priceField, gbc);

        // Row 4: Status
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        editPanel.add(statusCombo, gbc);
        // JPanel UI end here

        // Comfirm Dialog
        int result = JOptionPane.showConfirmDialog(
            parent,
            editPanel,
            "Edit Item",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        // Update logic here
        if (result == JOptionPane.OK_OPTION) {
            item.setName(nameField.getText().trim());
            item.setStockQuantity((int) stockSpinner.getValue());
            item.setPrice(Double.parseDouble(priceField.getText().trim()));
            item.setStatus((Item.Status) statusCombo.getSelectedItem());
            
            // Save changes to repository
            itemSupplierRepo.update(item);

            JOptionPane.showMessageDialog(parent, "Item updated successfully!");
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
