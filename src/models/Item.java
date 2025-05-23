/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Chan Yong Liang
 */
public class Item implements Identifiable {

    public enum Status {
        onSale,
        notOnSale
    };

    private String itemId;
    private String name;
    private int stockQuantity;
    private double price;
    private double sellPrice;
    private String supplierId;
    private Status status;

    public Item(String itemId, String name, int stockQuantity, double price, double sellPrice, String supplierId, Status status) {
        this.itemId = itemId;
        this.name = name;
        this.stockQuantity = stockQuantity;
        this.price = price;
        this.sellPrice = sellPrice;
        this.supplierId = supplierId;
        this.status = status;
    }

    @Override
    public String getId() {
        return itemId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    @Override
    public String toString() {
        return name;
    }
}
