/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import java.util.List;
import models.PurchaseOrder;
import models.Supplier;
import repository.ItemRepository;
import repository.ItemSupplierRepository;
import repository.PurchaseOrdersRepository;
import repository.SupplierRepository;

/**
 *
 * @author ngoh
 */
public class PurchaseOrderService {

    private final PurchaseOrdersRepository purchaseOrderRepo = new PurchaseOrdersRepository();
    private final SupplierRepository supplierRepo = new SupplierRepository();
    private final ItemRepository itemRepo = new ItemRepository();
    private final ItemSupplierRepository itemSupplierRepo = new ItemSupplierRepository();


    private String[] getSuppliers(String itemId) {
        List<Supplier> supplierList = itemSupplierRepo.getItemSupplier(itemId);
        String[] supplierOptions = new String[supplierList.size()];

        for (int i = 0; i < supplierList.size(); i++) {
            supplierOptions[i] = supplierList.get(i).getSupplierId();
        }

        return supplierOptions;
    }

    public void addApprovalListener(Component parent, PurchaseOrder po) {
        
        int currentQuantity = po.getQuantity();
        Supplier currentSupplier = supplierRepo.find(po.getSupplierId());

        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(2, 2, 5, 5));
        panel.setPreferredSize(new java.awt.Dimension(350, 60));
        javax.swing.JTextField quantityField = new javax.swing.JTextField(String.valueOf(currentQuantity));

        String[] supplierOptions = getSuppliers(po.getItemId());
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

                double newPrice = updatedQuantity * itemRepo.find(po.getItemId()).getPrice();

                PurchaseOrder updatedPO = new PurchaseOrder(po.getPurchaseOrderId(), po.getItemId(), 
                                                            po.getPurchaseRequisitionId(), po.getPurchaseManagerId(), 
                                                            Integer.parseInt(newQuantity), newPrice, po.getDate(), 
                                                            PurchaseOrder.Status.fulfilled, newSupplierId);

                purchaseOrderRepo.update(updatedPO);

                javax.swing.JOptionPane.showMessageDialog(parent, "Approved successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            } else {
                javax.swing.JOptionPane.showMessageDialog(parent, "Failed to approve. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

