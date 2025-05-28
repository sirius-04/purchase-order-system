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
import models.Item;
import models.Supplier;
import repository.ItemRepository;
import repository.ItemSupplierRepository;
import repository.SupplierRepository;
import utils.IdGenerator;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemService {

    private ItemSupplierRepository itemSupplierRepo = new ItemSupplierRepository();
    private SupplierRepository supplierRepo = new SupplierRepository();
    private ItemRepository itemRepo = new ItemRepository();
    private IdGenerator idGenerator = new IdGenerator();

    public void addItem(Component parent) {
        JTextField nameField = new JTextField(20);
        JSpinner stockSpinner = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));
        JTextField priceField = new JTextField();
        JTextField sellPriceField = new JTextField();

        String[] supplierOptions = getSupplierOptions();
        JComboBox<String> supplierCombo = new JComboBox<>(supplierOptions);

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
        itemPanel.add(new JLabel("Select Supplier ID:"));
        itemPanel.add(supplierCombo);

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
            try {
                String enteredName = nameField.getText().trim();
                int enteredQuantity = (Integer) stockSpinner.getValue();
                double enteredPrice = Double.parseDouble(priceField.getText().trim());
                double enteredSellPrice = Double.parseDouble(sellPriceField.getText().trim());
                Supplier selectedSupplier = supplierRepo.find((String) supplierCombo.getSelectedItem());

                if (enteredName.isEmpty() || selectedSupplier == null) {
                    throw new IllegalArgumentException("Name and supplier must be provided.");
                }

                // Prompt user for item status
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
                Item newItem = new Item(
                        generatedItemId,
                        enteredName,
                        enteredQuantity,
                        enteredPrice,
                        enteredSellPrice,
                        selectedSupplier.getSupplierId(),
                        status
                );

                itemRepo.save(newItem);

                JOptionPane.showMessageDialog(parent, "Item added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(parent, "Price and selling price must be valid numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(parent, e.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
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
}
