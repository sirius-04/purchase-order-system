/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemTableRow implements TableConvertible {
    private String itemId, itemName;
    private double price;
    private int stockQuantity;
    private String supplierName;

    public ItemTableRow(String itemId, String itemName, double price, int stockQuantity, String supplierName) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.supplierName = supplierName;
    }
    
    @Override
    public Object[] toTableRow() {
        return new Object[]{
            itemId, itemName, price, stockQuantity, supplierName
        };
    }
}
