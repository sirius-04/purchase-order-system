/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryUpdate implements Identifiable {

    private String inventoryUpdateId, itemId, supplierId;
    private int updateQuantity;
    private double totalAmount;

    public InventoryUpdate(String inventoryUpdateId, String itemId, String supplierId, int updateQuantity, double totalAmount) {
        this.inventoryUpdateId = inventoryUpdateId;
        this.itemId = itemId;
        this.supplierId = supplierId;
        this.updateQuantity = updateQuantity;
        this.totalAmount = totalAmount;
    }

    @Override
    public String getId() {
        return inventoryUpdateId;
    }

    public String getInventoryUpdateId() {
        return inventoryUpdateId;
    }

    public void setInventoryUpdateId(String inventoryUpdateId) {
        this.inventoryUpdateId = inventoryUpdateId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public int getUpdateQuantity() {
        return updateQuantity;
    }

    public void setUpdateQuantity(int updateQuantity) {
        this.updateQuantity = updateQuantity;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

}
