/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Chan Yong Liang
 */
public class PurchaseRequisition implements Identifiable {
    // field: requisitionID,salesManagerID,itemID,quantity,requiredDate,status(pending/approved/rejected)

    public enum Status {
        pending,
        approved,
        rejected
    }

    private String requisitionId;
    private String salesManagerId;
    private String itemId;
    private int quantity;
    private String generatedDate;
    private String requiredDate;
    private Status status;

    // constructor
    public PurchaseRequisition(String requisitionId, String salesManagerId, String itemId, int quantity, String generatedDate, String requiredDate, Status status) {
        this.requisitionId = requisitionId;
        this.salesManagerId = salesManagerId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.generatedDate = generatedDate;
        this.requiredDate = requiredDate;
        this.status = status;
    }

    // override interface
    @Override
    public String getId() {
        return requisitionId;
    }

    // Getters & Setters
    public String getRequisitionId() {
        return requisitionId;
    }

    public void setRequisitionId(String requisitionId) {
        this.requisitionId = requisitionId;
    }

    public String getSalesManagerId() {
        return salesManagerId;
    }

    public void setSalesManagerId(String salesManagerId) {
        this.salesManagerId = salesManagerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    public String getRequiredDate() {
        return requiredDate;
    }

    public void setRequiredDate(String requiredDate) {
        this.requiredDate = requiredDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
