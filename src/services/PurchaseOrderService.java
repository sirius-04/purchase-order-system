/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import java.util.List;
import javax.swing.JTable;
import models.Payment;
import models.PurchaseOrder;
import models.Supplier;
import repository.ItemRepository;
import repository.ItemSupplierRepository;
import repository.PaymentRepository;
import repository.PurchaseOrdersRepository;
import repository.SupplierRepository;
import tables.InventoryTableModel;
import tables.PaymentTableModel;
import tables.PurchaseOrderTableModel;
import utils.IdGenerator;

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
    
     private void createPaymentForPurchaseOrder(PurchaseOrder po) {
        PaymentRepository paymentRepo = new PaymentRepository();

        IdGenerator idGen = new IdGenerator();
        String newPaymentId = idGen.generateNewId(Payment.class);

        // Create Payment object and save to file
        Payment newPayment = new Payment(
                newPaymentId,
                po.getPurchaseOrderId(),
                po.getPrice(),
                java.time.LocalDate.now().toString(),
                models.Payment.Status.pending
        );

        paymentRepo.save(newPayment);
    }

    public void addApprovalListener(Component parent, JTable purchaseOrderTable, JTable inventoryTable) {
        purchaseOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = purchaseOrderTable.getSelectedRow();
                if (row != -1) {
                    showApprovalDialog(parent, purchaseOrderTable, inventoryTable, row);
                }
            }
        });
    }

    private void showApprovalDialog(Component parent, JTable purchaseOrderTable, JTable inventoryTable, int selectedRow) {
        PurchaseOrderTableModel purchaseOrderModel = (PurchaseOrderTableModel) purchaseOrderTable.getModel();
        InventoryTableModel inventoryModel = (InventoryTableModel) inventoryTable.getModel();
        PurchaseOrder selectedPO = purchaseOrderModel.getPurchaseOrderAt(selectedRow);

        String poId = selectedPO.getPurchaseOrderId();
        int currentQuantity = selectedPO.getQuantity();
        Supplier currentSupplier = supplierRepo.find(selectedPO.getSupplierId());

        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(2, 2, 5, 5));
        panel.setPreferredSize(new java.awt.Dimension(350, 60));
        javax.swing.JTextField quantityField = new javax.swing.JTextField(String.valueOf(currentQuantity));

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

                PurchaseOrder updatedPO = new PurchaseOrder(selectedPO.getPurchaseOrderId(), selectedPO.getItemId(), 
                                                            selectedPO.getPurchaseRequisitionId(), selectedPO.getPurchaseManagerId(), 
                                                            Integer.parseInt(newQuantity), newPrice, selectedPO.getDate(), 
                                                            PurchaseOrder.Status.fulfilled, newSupplierId);

                purchaseOrderRepo.update(updatedPO);

                javax.swing.JOptionPane.showMessageDialog(parent, "Approved successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                purchaseOrderModel.refresh();
                inventoryModel.refresh();

            } else {
                javax.swing.JOptionPane.showMessageDialog(parent, "Failed to approve. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void verifyUpdateListener(Component parent, JTable inventoryTable, JTable paymentTable) {
        inventoryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = inventoryTable.getSelectedRow();
                if (row != -1) {
                    InventoryTableModel inventoryModel = (InventoryTableModel) inventoryTable.getModel();
                    PaymentTableModel paymentModel = (PaymentTableModel) paymentTable.getModel();
                    PurchaseOrder selectedPO = inventoryModel.getPurchaseOrderAt(row);

                    int option = javax.swing.JOptionPane.showConfirmDialog(
                            parent,
                            "Do you want to verify this inventory update?",
                            "Verify update inventory",
                            javax.swing.JOptionPane.YES_NO_OPTION
                    );

                    if (option == javax.swing.JOptionPane.YES_OPTION) {
              
                        PurchaseOrder updatedPO = new PurchaseOrder(selectedPO.getPurchaseOrderId(), selectedPO.getItemId(), 
                                                                    selectedPO.getPurchaseRequisitionId(), selectedPO.getPurchaseManagerId(), 
                                                                    selectedPO.getQuantity(), selectedPO.getPrice(), selectedPO.getDate(), 
                                                                    PurchaseOrder.Status.verified, selectedPO.getSupplierId());

                        purchaseOrderRepo.update(updatedPO);
                        
                        //after verify create new payment
                        createPaymentForPurchaseOrder(updatedPO);

                        javax.swing.JOptionPane.showMessageDialog(parent, "Verify successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                            
                        inventoryModel.refresh();
                        paymentModel.refresh();

                    } else {
                        javax.swing.JOptionPane.showMessageDialog(parent, "Failed to verify. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }


    public boolean processPayment(Component parent, JTable paymentTable) {
        paymentTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = paymentTable.getSelectedRow();
                if (row != -1) {
                    PaymentTableModel paymentModel = (PaymentTableModel) paymentTable.getModel();
                    Payment selectedPayment = paymentModel.getPaymentAt(row);
                    
                    if (selectedPayment.getStatus() == Payment.Status.successed) {
                        javax.swing.JOptionPane.showMessageDialog(parent, "This payment has already been processed.", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }

                    PurchaseOrder po = purchaseOrderRepo.find(selectedPayment.getPurchaseOrderId());
                    Supplier supplier = supplierRepo.find(po.getSupplierId());

                    String accountNum = supplier.getAccountNum();
                    Double amountToPaid = selectedPayment.getAmountPaid();

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
                            return;
                        }

                        double enteredAmount;
                        
                        try {
                            enteredAmount = Double.parseDouble(enteredAmountStr);
                        } catch (NumberFormatException e) {
                            javax.swing.JOptionPane.showMessageDialog(parent, "Invalid amount format.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                                parent,
                                "Do you want to confirm this payment?",
                                "Confirm Payment",
                                javax.swing.JOptionPane.YES_NO_OPTION
                        );

                        if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                            Payment updatedPayment = new Payment(
                                    selectedPayment.getPaymentId(),
                                    selectedPayment.getPurchaseOrderId(),
                                    selectedPayment.getAmountPaid() - enteredAmount,
                                    java.time.LocalDate.now().toString(),
                                    models.Payment.Status.successed
                            );
                   
                            PaymentRepository paymentRepo = new PaymentRepository();
                            paymentRepo.update(updatedPayment);     

                            javax.swing.JOptionPane.showMessageDialog(parent, "Payment processed successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

                            paymentModel.refresh();
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(parent, "Payment not confirmed.", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });
        return false;
    }
}

