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
import models.ItemSupplier;
import repository.ItemRepository;
import repository.ItemSupplierRepository;
import utils.IdGenerator;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemService {

    private ItemRepository itemRepo = new ItemRepository();
    private ItemSupplierRepository itemSupplierRepo = new ItemSupplierRepository();
    private SupplierRepository supplierRepo = new SupplierRepository();
    private SupplierService supplierService = new SupplierService();
    private IdGenerator idGenerator = new IdGenerator();

    public void displayItemDetails(Component parent, Item item) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Item ID: " + item.getItemId()));
        panel.add(new JLabel("Item Name: " + item.getName()));
        panel.add(new JLabel("Stock Quantity: " + item.getStockQuantity()));
        panel.add(new JLabel("Cost Price: " + item.getPrice()));
        panel.add(new JLabel("Sell Price: " + item.getSellPrice()));
        panel.add(new JLabel("Supplier ID: " + item.getSupplierId()));
        panel.add(new JLabel("Status: " + item.getStatus()));

        Object[] options = {"Edit", "Delete", "Close"};
        int result = JOptionPane.showOptionDialog(
                parent,
                panel,
                "Item Details",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[2]
        );

        if (result == 0) {
            editItem(parent, item);
        } else if (result == 1) {
            deleteItem(parent, item);
        }
    }

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

        int itemResult = JOptionPane.showConfirmDialog(
                parent,
                itemPanel,
                "New Item - Step 1",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (itemResult != JOptionPane.OK_OPTION) {
            return;
        }

        String enteredName = nameField.getText().trim();
        int enteredQuantity = (Integer) stockSpinner.getValue();
        double enteredPrice, enteredSellPrice;
        try {
            enteredPrice = Double.parseDouble(priceField.getText().trim());
            enteredSellPrice = Double.parseDouble(sellPriceField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(parent, "Price and selling price must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] supplierOptions = getSupplierOptions();
        JComboBox<String> supplierCombo = new JComboBox<>(supplierOptions);

        Object[] supplierOptionsButtons = {"Confirm", "Select New Supplier", "Cancel"};
        JPanel supplierPanel = new JPanel();
        supplierPanel.setLayout(new BoxLayout(supplierPanel, BoxLayout.Y_AXIS));
        supplierPanel.add(new JLabel("Select Supplier ID:"));
        supplierPanel.add(supplierCombo);

        int supplierChoice = JOptionPane.showOptionDialog(
                parent,
                supplierPanel,
                "New Item - Step 2",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                supplierOptionsButtons,
                supplierOptionsButtons[0]
        );

        Supplier selectedSupplier = null;

        if (supplierChoice == 2 || supplierChoice == JOptionPane.CLOSED_OPTION) {
            return;
        } else if (supplierChoice == 1) {
            selectedSupplier = supplierService.addSupplier(parent);
            if (selectedSupplier == null) {
                return;
            }
        } else {
            selectedSupplier = supplierRepo.find((String) supplierCombo.getSelectedItem());
        }

        Object[] statusOptions = {"On Sale", "Not On Sale"};
        int statusResult = JOptionPane.showOptionDialog(
                parent,
                "Should this item be marked as on sale?",
                "Item Status",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                statusOptions,
                statusOptions[1]
        );

        Item.Status status = (statusResult == 0) ? Item.Status.onSale : Item.Status.notOnSale;

        String generatedItemId = idGenerator.generateNewId(Item.class);
        Item newItem = new Item(generatedItemId, enteredName, enteredQuantity, enteredPrice, enteredSellPrice, selectedSupplier.getSupplierId(), status);
        itemRepo.save(newItem);

        ItemSupplier itemSupplier = new ItemSupplier(generatedItemId, selectedSupplier.getSupplierId());
        itemSupplierRepo.save(itemSupplier);

        JOptionPane.showMessageDialog(parent, "Item and Supplier saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public void editItem(Component parent, Item item) {
        if (item == null) {
            return;
        }

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
            itemRepo.update(item);

            JOptionPane.showMessageDialog(parent, "Item updated successfully!");
        }
    }
    public void editItemQuantity(Component parent, Item item) {
        if (item == null) {
            return;
        }

        // Jpanel's Input
        JSpinner stockSpinner = new JSpinner(new SpinnerNumberModel(item.getStockQuantity(), 0, 10000, 1));
        
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

        // Row 1: Stock Quantity
        gbc.gridx = 0;
        gbc.gridy++;
        editPanel.add(new JLabel("Stock Quantity:"), gbc);
        gbc.gridx = 1;
        editPanel.add(stockSpinner, gbc);

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
            item.setStockQuantity((int) stockSpinner.getValue());

            // Save changes to repository
            itemRepo.update(item);

            JOptionPane.showMessageDialog(parent, "Item updated successfully!");
        }
    }

    private void deleteItem(Component parent, Item item) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Confirm delete item:"));
        panel.add(new JLabel("Item ID: " + item.getItemId()));
        panel.add(new JLabel("Item Name: " + item.getName()));

        int result = JOptionPane.showConfirmDialog(parent, panel, "Delete Item", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int deleteItem = JOptionPane.showConfirmDialog(
                    parent,
                    "Do you sure you want to delete the item?",
                    "Delete Item",
                    JOptionPane.YES_NO_OPTION
            );

            if (deleteItem == JOptionPane.YES_OPTION) {
                item.setStatus(Item.Status.deleted);
                itemRepo.update(item);

                JOptionPane.showMessageDialog(parent, "Item deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }

    private String[] getSupplierOptions() {
        List<Supplier> supplierList = supplierRepo.getAll().stream().filter(supplier -> supplier.getStatus() == Supplier.Status.active).toList();
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
