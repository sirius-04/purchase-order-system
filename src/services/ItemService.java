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
import models.ItemSupplier;
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
            JTextField sNameField = new JTextField();
            JTextField sContactField = new JTextField();
            JTextField sEmailField = new JTextField();
            JTextField sBankField = new JTextField();

            JPanel newSupplierPanel = new JPanel();
            newSupplierPanel.setLayout(new BoxLayout(newSupplierPanel, BoxLayout.Y_AXIS));
            newSupplierPanel.add(new JLabel("Supplier Name:"));
            newSupplierPanel.add(sNameField);
            newSupplierPanel.add(new JLabel("Contact Number:"));
            newSupplierPanel.add(sContactField);
            newSupplierPanel.add(new JLabel("Email:"));
            newSupplierPanel.add(sEmailField);
            newSupplierPanel.add(new JLabel("Bank Account Number:"));
            newSupplierPanel.add(sBankField);

            int newSupplierResult = JOptionPane.showConfirmDialog(
                    parent,
                    newSupplierPanel,
                    "Create New Supplier",
                    JOptionPane.OK_CANCEL_OPTION
            );

            if (newSupplierResult != JOptionPane.OK_OPTION) {
                return;
            }

            String supplierName = sNameField.getText().trim();
            String contact = sContactField.getText().trim();
            String email = sEmailField.getText().trim();
            String bankAccount = sBankField.getText().trim();

            if (supplierName.isEmpty() || contact.isEmpty() || email.isEmpty() || bankAccount.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "All supplier fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String newSupplierId = idGenerator.generateNewId(Supplier.class);
            selectedSupplier = new Supplier(newSupplierId, supplierName, contact, email, bankAccount);
            supplierRepo.save(selectedSupplier);
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

    private String[] getSupplierOptions() {
        List<Supplier> supplierList = supplierRepo.getAll();
        String[] supplierOptions = new String[supplierList.size()];

        for (int i = 0; i < supplierList.size(); i++) {
            supplierOptions[i] = supplierList.get(i).getSupplierId();
        }

        return supplierOptions;
    }
}
