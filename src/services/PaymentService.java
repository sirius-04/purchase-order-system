/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import models.InventoryUpdate;
import models.Payment;
import models.Supplier;
import repository.InventoryUpdateRepository;
import repository.PaymentRepository;
import repository.SupplierRepository;

/**
 *
 * @author ngoh
 */
public class PaymentService {
        
    private final SupplierRepository supplierRepo = new SupplierRepository();
    private final InventoryUpdateRepository inventoryRepo = new InventoryUpdateRepository();
        
    public void processPayment(Component parent, Payment payment) {

        if (payment.getStatus() == Payment.Status.successed) {
            javax.swing.JOptionPane.showMessageDialog(parent, "This payment has already been processed.", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
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
            double enteredAmount = 0;

            if (enteredAccountNum.isEmpty() || enteredAmountStr.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(parent, "All fields are required.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            }

            try {
                enteredAmount = Double.parseDouble(enteredAmountStr);
            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(parent, "Invalid amount format.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
            
            if (enteredAmount > amountToPaid) {
            javax.swing.JOptionPane.showMessageDialog(parent, "Entered amount exceeds the payable amount.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
            }

            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    parent,
                    "Do you want to confirm this payment?",
                    "Confirm Payment",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );
            
            double balanceAmount = payment.getAmountPaid() - enteredAmount;
            Payment.Status status = Payment.Status.pending;

            if (confirm == javax.swing.JOptionPane.YES_OPTION && balanceAmount == 0) {
                status = Payment.Status.successed;
            }
            
            Payment updatedPayment = new Payment(
                       payment.getPaymentId(),
                       inventoryUpdate.getInventoryUpdateId(),
                       balanceAmount,
                       java.time.LocalDate.now().toString(),
                       status
               );

            PaymentRepository paymentRepo = new PaymentRepository();
            paymentRepo.update(updatedPayment);     

            javax.swing.JOptionPane.showMessageDialog(parent, "Payment processed successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                javax.swing.JOptionPane.showMessageDialog(parent, "Payment not finished.", "Info", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            }
        }
}
