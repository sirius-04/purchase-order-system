/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Chan Yong Liang
 */
public class Sales implements Identifiable {
    // field: saleID,itemID,quantity,date,totalAmount
    
    private String salesId;
    private String itemId;
    private int quantity;
    private String date;
    private String time;
    private int totalAmount;
    
    // constructor
    public Sales(String salesId, String itemId, int quantity, String date, String time, int totalAmount) {
        this.salesId = salesId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.date = date;
        this.time = time;
        this.totalAmount = totalAmount;
    }
    
    // override interface
    @Override
    public String getId() {
        return salesId;
    }

    // Getters & Setters
    public String getSalesId() {
        return salesId;
    }

    public void setSalesId(String salesId) {
        this.salesId = salesId;
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
}
