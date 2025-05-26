/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import helpers.PurchaseOrderTableHelper;
import java.awt.Component;
import javax.swing.JTable;
import repository.PurchaseOrdersRepository;
import utils.FileManager;
import utils.IdGenerator;

/**
 *
 * @author ngoh
 */
public class PurchaseOrderService {
    private final IdGenerator idGenerator = new IdGenerator();
    private final PurchaseOrdersRepository purchaseOrderRepo = new PurchaseOrdersRepository();
    private final FileManager fileManager = new FileManager();
    
    private int getColumnIndex(JTable table, String columnName) {
    for (int i = 0; i < table.getColumnCount(); i++) {
        if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
            return i;
        }
    }
    return -1;
}
    
    public boolean approvePo(String poId, String newQuantity, String newSupplier) {
        String[] lines = fileManager.readFile("purchase_orders");
        
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String[] parts = line.split(",");
            
            if (parts[0].equals(poId)) {
                parts[4] = newQuantity;
                parts[7] = "fullfilled";
                parts[8] = newSupplier;
                String updatedLine = String.join(",", parts);
                
                fileManager.editFile("purchase_orders", i + 1, updatedLine);
                return true;
            }
        }
        return false;
     }
     
    private String[] getSuppliers() {
        String[] lines = fileManager.readFile("suppliers");
        String[] supplierOptions = new String[lines.length];

        for (int i = 0; i < lines.length; i++) {
            String[] parts = lines[i].split(",");
            supplierOptions[i] = parts[0];
        }

        return supplierOptions;
    }

    
   public void addApprovalListener(Component parent, JTable purchaseOrderTable, Runnable refreshInventoryTableCallback) {
    purchaseOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = purchaseOrderTable.getSelectedRow();
            if (row != -1) {
                showApprovalDialog(parent, purchaseOrderTable, row, refreshInventoryTableCallback);
                }
            }
        });
    }

    private void showApprovalDialog(Component parent, JTable table, int row, Runnable refreshCallback) {
        String poId = table.getValueAt(row, getColumnIndex(table, "Purchase Order ID")).toString();
        String currentQuantity = table.getValueAt(row, getColumnIndex(table, "Quantity")).toString();
        String currentSupplier = table.getValueAt(row, getColumnIndex(table, "Supplier ID")).toString();

        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(2, 2, 5, 5));
        javax.swing.JTextField quantityField = new javax.swing.JTextField(currentQuantity);

        String[] supplierOptions = getSuppliers();
        javax.swing.JComboBox<String> supplierCombo = new javax.swing.JComboBox<>(supplierOptions);
        for (int i = 0; i < supplierOptions.length; i++) {
            if (supplierOptions[i].contains(currentSupplier)) {
                supplierCombo.setSelectedIndex(i);
                break;
            }
        }

        panel.add(new javax.swing.JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new javax.swing.JLabel("Supplier:"));
        panel.add(supplierCombo);

        int option = javax.swing.JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Edit Purchase Order",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE
        );

        if (option == javax.swing.JOptionPane.OK_OPTION) {
            String newQuantity = quantityField.getText();
            String newSupplierId = supplierCombo.getSelectedItem().toString();

            if (newQuantity.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(parent, "Quantity cannot be empty.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    parent,
                    "Do you want to approve this purchase order?",
                    "Approve Purchase Order",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                boolean success = approvePo(poId, newQuantity, newSupplierId);

                if (success) {
                    javax.swing.JOptionPane.showMessageDialog(parent, "Approved successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    ((javax.swing.table.DefaultTableModel) table.getModel()).removeRow(row);
                    refreshCallback.run();
                } else {
                    javax.swing.JOptionPane.showMessageDialog(parent, "Failed to approve. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    public void verifyUpdateListener(Component parent, JTable inventoryTable) {
    inventoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = inventoryTable.getSelectedRow();
            if (row != -1) {
                int option = javax.swing.JOptionPane.showConfirmDialog(
                        parent,
                        "Do you want to verify this inventory update?",
                        "Verify update inventory",
                        javax.swing.JOptionPane.YES_NO_OPTION
                );

                if (option == javax.swing.JOptionPane.YES_OPTION) {
                    String poId = inventoryTable.getValueAt(row, getColumnIndex(inventoryTable, "Purchase Order ID")).toString();
                    boolean success = verifyInventoryUpdate(poId);

                    if (success) {
                        javax.swing.JOptionPane.showMessageDialog(
                                parent,
                                "Verify successfully!",
                                "Success",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );

                        PurchaseOrderTableHelper.populatePurchaseOrder(inventoryTable, models.PurchaseOrder.Status.fullfilled);
                    
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(
                                parent,
                                "Failed to verify. Please try again.",
                                "Error",
                                javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        }
    });
}
    public boolean verifyInventoryUpdate(String poId) {
        return true;
    }
    
    public boolean processPayment (Component parent, JTable paymentTable) {
        return true;
    }
}
