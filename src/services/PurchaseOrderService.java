/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import java.util.List;
import javax.swing.JTable;
import models.PurchaseOrder;
import models.Supplier;
import repository.ItemRepository;
import repository.ItemSupplierRepository;
import repository.PurchaseOrdersRepository;
import repository.SupplierRepository;
import tables.InventoryTableModel;
import tables.PurchaseOrderTableModel;

/**
 *
 * @author ngoh
 */
public class PurchaseOrderService {

    private final PurchaseOrdersRepository purchaseOrderRepo = new PurchaseOrdersRepository();
    private final SupplierRepository supplierRepo = new SupplierRepository();
    private final ItemRepository itemRepo = new ItemRepository();
    private final ItemSupplierRepository itemSupplierRepo = new ItemSupplierRepository();

    private int getColumnIndex(JTable table, String columnName) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
                return i;
            }
        }
        return -1;
    }

    private String[] getSuppliers(String itemId) {
        List<Supplier> supplierList = itemSupplierRepo.getItemSupplier(itemId);
        String[] supplierOptions = new String[supplierList.size()];

        for (int i = 0; i < supplierList.size(); i++) {
            supplierOptions[i] = supplierList.get(i).getSupplierId();
        }

        return supplierOptions;
    }

    public void addApprovalListener(Component parent, JTable purchaseOrderTable) {
        purchaseOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = purchaseOrderTable.getSelectedRow();
                if (row != -1) {
                    showApprovalDialog(parent, purchaseOrderTable, row);
                }
            }
        });
    }

    private void showApprovalDialog(Component parent, JTable table, int selectedRow) {
        PurchaseOrderTableModel model = (PurchaseOrderTableModel) table.getModel();
        PurchaseOrder selectedPO = model.getPurchaseOrderAt(selectedRow);

        String poId = selectedPO.getPurchaseOrderId();
        int currentQuantity = selectedPO.getQuantity();
        Supplier currentSupplier = supplierRepo.find(selectedPO.getSupplierId());

        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(2, 2, 5, 5));
        javax.swing.JTextField quantityField = new javax.swing.JTextField(currentQuantity);

        String[] supplierOptions = getSuppliers(selectedPO.getItemId());
        javax.swing.JComboBox<String> supplierCombo = new javax.swing.JComboBox<>(supplierOptions);
        for (int i = 0; i < supplierOptions.length; i++) {
            if (supplierOptions[i].contains(currentSupplier.getName())) {
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
                int updatedQuantity = Integer.parseInt(newQuantity);

                double newPrice = updatedQuantity * itemRepo.find(selectedPO.getItemId()).getPrice();

                PurchaseOrder updatedPO = new PurchaseOrder(selectedPO.getPurchaseOrderId(), selectedPO.getItemId(), selectedPO.getPurchaseRequisitionId(), selectedPO.getPurchaseManagerId(), Integer.parseInt(newQuantity), newPrice, selectedPO.getDate(), PurchaseOrder.Status.fullfilled, newSupplierId);

                purchaseOrderRepo.update(updatedPO);

                javax.swing.JOptionPane.showMessageDialog(parent, "Approved successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                model.refresh();

            } else {
                javax.swing.JOptionPane.showMessageDialog(parent, "Failed to approve. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void verifyUpdateListener(Component parent, JTable inventoryTable) {
        inventoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = inventoryTable.getSelectedRow();
                if (row != -1) {
                    InventoryTableModel model = (InventoryTableModel) inventoryTable.getModel();
                    PurchaseOrder selectedPO = model.getPurchaseOrderAt(row);

                    int option = javax.swing.JOptionPane.showConfirmDialog(
                            parent,
                            "Do you want to verify this inventory update?",
                            "Verify update inventory",
                            javax.swing.JOptionPane.YES_NO_OPTION
                    );

                    if (option == javax.swing.JOptionPane.YES_OPTION) {
                        String poId = selectedPO.getPurchaseOrderId();
                        boolean success = verifyInventoryUpdate(poId);

                        if (success) {
                            javax.swing.JOptionPane.showMessageDialog(
                                    parent,
                                    "Verify successfully!",
                                    "Success",
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                            );

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

    public boolean processPayment(Component parent, JTable paymentTable) {
        return true;
    }
}
