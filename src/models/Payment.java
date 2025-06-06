/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;
/**
 *
 * @author Chan Yong Liang
 */
public class Payment implements Identifiable {
    // fields: paymentID,poID,amountPaid,datePaid,status(successed,failed)
    
    public enum Status {
        successed,
        failed,
        pending
    };
    
    private String paymentId;
    private String inventoryUpdateId;
    private double amountPaid;
    private String datePaid;
    private Status status;

    // constructor
    public Payment(String paymentId, String inventoryUpdateId, double amountPaid, String datePaid, Status status) {
        this.paymentId = paymentId;
        this.inventoryUpdateId = inventoryUpdateId;
        this.amountPaid = amountPaid;
        this.datePaid = datePaid;
        this.status = status;
    }
    
    // override interface
    @Override
    public String getId() {
        return paymentId;
    }

    // getter & setter
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getInventoryUpdateId() {
        return inventoryUpdateId;
    }

    public void setPurchaseOrderId(String inventoryUpdateId) {
        this.inventoryUpdateId = inventoryUpdateId;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public String getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(String datePaid) {
        this.datePaid = datePaid;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

