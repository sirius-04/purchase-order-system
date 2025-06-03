/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import java.awt.Dimension;
import java.util.List;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
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
public class SupplierService {

    private final IdGenerator idGenerator = new IdGenerator();
    private final SupplierRepository supplierRepo = new SupplierRepository();
    private final ItemSupplierRepository itemSupplierRepo = new ItemSupplierRepository();
    private final ItemRepository itemRepo = new ItemRepository();

    public void displaySupplierDetails(Component parent, Supplier supplier) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Supplier ID: " + supplier.getSupplierId()));
        panel.add(new JLabel("Supplier Name: " + supplier.getName()));
        panel.add(new JLabel("Contact Number: " + supplier.getContactNum()));
        panel.add(new JLabel("Email: " + supplier.getEmail()));
        panel.add(new JLabel("Bank Account Number: " + supplier.getAccountNum()));

        String[] suppliedItems = getSuppliedItem(supplier.getSupplierId());

        panel.add(Box.createVerticalStrut(10));
        panel.add(new JLabel("Items Supplied:"));

        if (suppliedItems.length == 0) {
            panel.add(new JLabel("  (No items found)"));
        } else {
            JList<String> itemList = new JList<>(suppliedItems);
            itemList.setVisibleRowCount(Math.min(suppliedItems.length, 5));
            itemList.setEnabled(false);

            JScrollPane scrollPane = new JScrollPane(itemList);
            scrollPane.setPreferredSize(new Dimension(250, 80));
            panel.add(scrollPane);
        }

        Object[] options = {"Edit", "Delete", "Close"};
        int result = JOptionPane.showOptionDialog(
                parent,
                panel,
                "Supplier Details",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[2]
        );

        if (result == 0) {
            editSupplier(parent, supplier);
        } else if (result == 1) {
            deleteSupplier(parent, supplier);
        }
    }

    public Supplier addSupplier(Component parent) {
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
            return null;
        }

        String supplierName = sNameField.getText().trim();
        String contact = sContactField.getText().trim();
        String email = sEmailField.getText().trim();
        String bankAccount = sBankField.getText().trim();

//        if (supplierRepo.checkNameExists(supplierName)) {
//            JOptionPane.showMessageDialog(parent, "Supplier name already exists. Please choose a different name.", "Duplicate Name", JOptionPane.ERROR_MESSAGE);
//            return null;
//        }

        if (supplierName.isEmpty() || contact.isEmpty() || email.isEmpty() || bankAccount.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "All supplier fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String newSupplierId = idGenerator.generateNewId(Supplier.class);
        Supplier newSupplier = new Supplier(newSupplierId, supplierName, contact, email, bankAccount, Supplier.Status.active);
        supplierRepo.save(newSupplier);
        return newSupplier;
    }

    public void editSupplier(Component parent, Supplier supplier) {
        JTextField nameField = new JTextField(supplier.getName());
        JTextField contactField = new JTextField(supplier.getContactNum());
        JTextField emailField = new JTextField(supplier.getEmail());
        JTextField bankField = new JTextField(supplier.getAccountNum());

        List<Item> allItems = itemRepo.getAll().stream()
                .filter(item -> item.getStatus() != Item.Status.deleted)
                .toList();

        List<Item> currentSuppliedItems = itemSupplierRepo.getSupplierItem(supplier.getSupplierId());

        DefaultListModel<Item> listModel = new DefaultListModel<>();
        for (Item item : allItems) {
            listModel.addElement(item);
        }

        JList<Item> itemJList = new JList<>(listModel);
        itemJList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        itemJList.setVisibleRowCount(5);
        itemJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                if (value instanceof Item) {
                    value = ((Item) value).getName();
                }
                return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            }
        });

        for (int i = 0; i < allItems.size(); i++) {
            if (currentSuppliedItems.contains(allItems.get(i))) {
                itemJList.addSelectionInterval(i, i);
            }
        }

        JScrollPane itemScroll = new JScrollPane(itemJList);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Edit Supplier Details:"));
        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Contact Number:"));
        panel.add(contactField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Bank Account Number:"));
        panel.add(bankField);
        panel.add(new JLabel("Items Supplied:"));
        panel.add(itemScroll);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Edit Supplier", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String email = emailField.getText().trim();
            String bank = bankField.getText().trim();

//            if (!name.equals(supplier.getName())) {
//                if (supplierRepo.checkNameExists(name)) {
//                    JOptionPane.showMessageDialog(parent, "Supplier name already exists. Please choose a different name.", "Duplicate Name", JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//            }

            if (name.isEmpty() || contact.isEmpty() || email.isEmpty() || bank.isEmpty()) {
                JOptionPane.showMessageDialog(parent, "All fields must be filled.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
                JOptionPane.showMessageDialog(parent, "Invalid email format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!contact.matches("\\d{7,15}")) {
                JOptionPane.showMessageDialog(parent, "Contact number must be 7-15 digits.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            supplier.setName(name);
            supplier.setContactNum(contact);
            supplier.setEmail(email);
            supplier.setAccountNum(bank);
            supplierRepo.update(supplier);

            List<Item> selectedItems = itemJList.getSelectedValuesList();
            List<ItemSupplier> allLinks = itemSupplierRepo.getAll();

            for (ItemSupplier link : allLinks) {
                if (link.getSupplierId().equals(supplier.getSupplierId())) {
                    itemSupplierRepo.delete(link);
                }
            }

            for (Item item : selectedItems) {
                itemSupplierRepo.save(new ItemSupplier(item.getItemId(), supplier.getSupplierId()));
            }

            JOptionPane.showMessageDialog(parent, "Supplier updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteSupplier(Component parent, Supplier supplier) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Confirm delete supplier:"));
        panel.add(new JLabel("Supplier ID: " + supplier.getSupplierId()));
        panel.add(new JLabel("Supplier Name: " + supplier.getName()));

        int result = JOptionPane.showConfirmDialog(parent, panel, "Delete Supplier", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int deleteSupplier = JOptionPane.showConfirmDialog(
                    parent,
                    "Do you sure you want to delete the supplier?",
                    "Delete Supplier",
                    JOptionPane.YES_NO_OPTION
            );

            if (deleteSupplier == JOptionPane.YES_OPTION) {
                supplier.setStatus(Supplier.Status.deleted);
                supplierRepo.update(supplier);

                JOptionPane.showMessageDialog(parent, "Supplier deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private String[] getSuppliedItem(String supplierId) {
        List<Item> itemList = itemSupplierRepo.getSupplierItem(supplierId);
        String[] itemNames = new String[itemList.size()];

        for (int i = 0; i < itemList.size(); i++) {
            itemNames[i] = itemList.get(i).getName();
        }

        return itemNames;
    }
}
