/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

import models.Payment;

/**
 *
 * @author ngoh
 */

// less account number

public class PaymentTableRow implements TableConvertible  {
    private String supplierId;
    private String supplierName;
    private String email;
    private String contactNum;
    private double amountPaid;
    private Payment.Status status;

    public PaymentTableRow(String supplierId, String supplierName, String email, String contactNum, double amountPaid, Payment.Status status) {
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.email = email;
        this.contactNum = contactNum;
        this.amountPaid = amountPaid;
        this.status = status;
    }

    @Override
    public Object[] toTableRow() {
        return new Object[]{
            supplierId, supplierName, email, contactNum, amountPaid, status
        };
    }
}
