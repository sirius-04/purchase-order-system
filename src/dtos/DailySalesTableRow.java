/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 *
 * @author Chan Yong Liang
 */
public class DailySalesTableRow implements TableConvertible {

    private String time;
    private String saleId;
    private String itemId;
    private String itemName;
    private int quantity;
    private double pricePerUnit;
    private double amount;

    public DailySalesTableRow(String time, String saleId, String itemId, String itemName,int quantity, double pricePerUnit, double amount) {
        this.time = time;
        this.saleId = saleId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
        this.amount = amount;
    }

    @Override
    public Object[] toTableRow() {
        return new Object[]{
            time, saleId, itemId, itemName, quantity, pricePerUnit, amount
        };
    }
}
