/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import models.InventoryUpdate;
import models.Payment;
import repository.InventoryUpdateRepository;
import repository.PaymentRepository;
import utils.IdGenerator;

/**
 *
 * @author ngoh
 */
public class InventoryUpdateService {
    private final InventoryUpdateRepository inventoryRepo = new InventoryUpdateRepository();
    
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
}
