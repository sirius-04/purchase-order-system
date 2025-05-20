/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package repository;

import models.Payment;

/**
 *
 * @author dede
 */
public class PaymentRepository extends BaseRepository<Payment> {
    
    public PaymentRepository() {
        super("payments", "%s,%s,%.2f,%s,%s");
    }
    
     @Override
    protected String formatToRow(Payment payment) {
        return String.format(rowFormat,
                payment.getPaymentId(),
                payment.getPurchaseOrderId(),
                payment.getAmountPaid(),
                payment.getDatePaid(),
                payment.getStatus()
        );
    }

    @Override
    protected Payment parseRow(String[] columns) {
        String paymentId = columns[0].trim();
        String purchaseOrderId = columns[1].trim();
        double amountPaid = Double.parseDouble(columns[2].trim());
        String datePaid = columns[3].trim();
        Payment.Status status = Payment.Status.valueOf(columns[4].trim());

        return new Payment(paymentId, purchaseOrderId, amountPaid, datePaid, status);
    }
    
}
