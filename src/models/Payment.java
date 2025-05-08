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
    private String paymentId;
    private String purchaseOrderId;
    private double amountPaid;
    private String datePaid;
    private String status;

    // constructor
    public Payment(String paymentId, String purchaseOrderId, double amountPaid, String datePaid, String status) {
        this.paymentId = paymentId;
        this.purchaseOrderId = purchaseOrderId;
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

    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

