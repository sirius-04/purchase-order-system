/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import models.users.UserRole;

/**
 *
 * @author Chan Yong Liang
 */
public class PurchaseOrder implements Identifiable {

    public enum Status {
        pending,
        approved,
        verified,
        deleted
    }

    private String purchaseOrderId;
    private String itemId;
    private String purchaseRequisitionId;
    private String userId;
    private int quantity;
    private double price;
    private String date;
    private Status status;
    private String supplierId;
    private String editedByUserId;
    private UserRole editedByRole;

    // constructor
    public PurchaseOrder(String purchaseOrderId, String itemId, String purchaseRequisitionId,
            String userId, int quantity, double price, String date, Status status, String supplierId) {
        this.purchaseOrderId = purchaseOrderId;
        this.itemId = itemId;
        this.purchaseRequisitionId = purchaseRequisitionId;
        this.userId = userId;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
        this.status = status;
        this.supplierId = supplierId;
    }

    // override interface
    @Override
    public String getId() {
        return purchaseOrderId;
    }

    // Getters and Setters
    public String getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public void setPurchaseOrderId(String purchaseOrderId) {
        this.purchaseOrderId = purchaseOrderId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPurchaseRequisitionId() {
        return purchaseRequisitionId;
    }

    public void setPurchaseRequisitionId(String purchaseRequisitionId) {
        this.purchaseRequisitionId = purchaseRequisitionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String purchaseManagerId) {
        this.userId = purchaseManagerId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getSupplierId() {
        return supplierId;
    }
    
    public String getEditedByUserId() {
        return editedByUserId;
    }

    public void setEditedByUserId(String editedByUserId) {
        this.editedByUserId = editedByUserId;
    }

    public UserRole getEditedByRole() {
        return editedByRole;
    }

    public void setEditedByRole(UserRole editedByRole) {
        this.editedByRole = editedByRole;
    }
}
