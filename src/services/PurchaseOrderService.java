/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import java.util.List;
import models.InventoryUpdate;
import models.Payment;
import models.PurchaseOrder;
import models.Supplier;
import repository.InventoryUpdateRepository;
import repository.ItemRepository;
import repository.ItemSupplierRepository;
import repository.PaymentRepository;
import repository.PurchaseOrdersRepository;
import repository.SupplierRepository;
import utils.IdGenerator;

/**
 *
 * @author ngoh
 */
public class PurchaseOrderService {

    private final PurchaseOrdersRepository purchaseOrderRepo = new PurchaseOrdersRepository();
    private final SupplierRepository supplierRepo = new SupplierRepository();
    private final InventoryUpdateRepository inventoryRepo = new InventoryUpdateRepository();
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
    
     private void createPaymentForInventoryUpdate(InventoryUpdate inventory) {
        PaymentRepository paymentRepo = new PaymentRepository();

        IdGenerator idGen = new IdGenerator();
        String newPaymentId = idGen.generateNewId(Payment.class);

        // Create Payment object and save to file
        Payment newPayment = new Payment(
                newPaymentId,
                inventory.getInventoryUpdateId(),
                inventory.getTotalAmount(),
                java.time.LocalDate.now().toString(),
                models.Payment.Status.pending
        );

        paymentRepo.save(newPayment);
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

    public void verifyUpdate(Component parent, InventoryUpdate inventory) {
        int option = javax.swing.JOptionPane.showConfirmDialog(
                parent,
                "Do you want to process payment?",
                "Verify update inventory",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (option == javax.swing.JOptionPane.YES_OPTION) {

            InventoryUpdate updatedInventory = new InventoryUpdate(inventory.getInventoryUpdateId(), inventory.getItemId(), 
                                               inventory.getSupplierId(), inventory.getUpdateQuantity(), inventory.getTotalAmount());

            inventoryRepo.update(updatedInventory);

            //after verify create new payment
            createPaymentForInventoryUpdate(updatedInventory);

            javax.swing.JOptionPane.showMessageDialog(parent, "Please go to payment", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

        } else {
            javax.swing.JOptionPane.showMessageDialog(parent, "Failed to process. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }


    public boolean processPayment(Component parent, Payment payment) {
                            
        if (payment.getStatus() == Payment.Status.successed) {
            javax.swing.JOptionPane.showMessageDialog(parent, "This payment has already been processed.", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        }

        InventoryUpdate inventoryUpdate = inventoryRepo.find(payment.getInventoryUpdateId());
        Supplier supplier = supplierRepo.find(inventoryUpdate.getSupplierId());

        String accountNum = supplier.getAccountNum();
        Double amountToPaid = payment.getAmountPaid();

        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(2, 2, 5, 5));
        panel.setPreferredSize(new java.awt.Dimension(350, 100));
        javax.swing.JTextField accountNumField = new javax.swing.JTextField(accountNum);
        javax.swing.JTextField amountToPaidField = new javax.swing.JTextField(String.valueOf(amountToPaid));

        panel.add(new javax.swing.JLabel("Account Number:"));
        panel.add(accountNumField);
        panel.add(new javax.swing.JLabel("Amount To Paid:"));
        panel.add(amountToPaidField);

        int option = javax.swing.JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Process Payment",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE
        );

        if (option == javax.swing.JOptionPane.OK_OPTION) {
            String enteredAccountNum = accountNumField.getText();
            String enteredAmountStr = amountToPaidField.getText();

            if (enteredAccountNum.isEmpty() || enteredAmountStr.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(parent, "All fields are required.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            double enteredAmount = 0;

            try {
                enteredAmount = Double.parseDouble(enteredAmountStr);
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(parent, "Invalid amount format.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }

            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    parent,
                    "Do you want to confirm this payment?",
                    "Confirm Payment",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                Payment updatedPayment = new Payment(
                        payment.getPaymentId(),
                        inventoryUpdate.getInventoryUpdateId(),
                        payment.getAmountPaid() - enteredAmount,
                        java.time.LocalDate.now().toString(),
                        models.Payment.Status.successed
                );

                PaymentRepository paymentRepo = new PaymentRepository();
                paymentRepo.update(updatedPayment);     

                javax.swing.JOptionPane.showMessageDialog(parent, "Payment processed successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            } else {
                javax.swing.JOptionPane.showMessageDialog(parent, "Payment not confirmed.", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }
    return false;
    }
}

