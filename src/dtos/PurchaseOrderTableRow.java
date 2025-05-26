/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

import models.PurchaseOrder;

/**
 *
 * @author ngoh
 */
public class PurchaseOrderTableRow implements TableConvertible  {
    private String purchaseOrderId;
    private String itemId;
    private String itemName;
    private int quantity;
    private double price;
    private String purchaseManagerId;
    private PurchaseOrder.Status status;
    private String supplierId;

    public PurchaseOrderTableRow(String purchaseOrderId, String itemId, String itemName, int quantity, 
                                 double price, String purchaseManagerId, PurchaseOrder.Status status, String supplierId) {
        this.purchaseOrderId = purchaseOrderId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.purchaseManagerId = purchaseManagerId;
        this.status = status;
        this.supplierId = supplierId;
    }

    @Override
    public Object[] toTableRow() {
        return new Object[]{
            purchaseOrderId, itemId, itemName, quantity, price, purchaseManagerId, status, supplierId
        };
    }
}
