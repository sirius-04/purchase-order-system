/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import models.Sales;

/**
 *
 * @author dede
 */
public class SalesRepository extends BaseRepository<Sales> {
    
     public SalesRepository() {
        super("sales", "%s,%s,%d,%s,%s,%.2f");
    }
    
     @Override
    protected String formatToRow(Sales sales) {
        return String.format(rowFormat,
                sales.getSalesId(),
                sales.getItemId(),
                sales.getQuantity(),
                sales.getDate(),
                sales.getTime(),
                sales.getTotalAmount()
        );
    }

    @Override
    protected Sales parseRow(String[] columns) {
        String salesId = columns[0].trim();
        String itemId = columns[1].trim();
        int quantity = Integer.parseInt(columns[2].trim());
        String date = columns[3].trim();
        String time = columns[4].trim();
        Double totalAmount = Double.valueOf(columns[5].trim());

        return new Sales(salesId, itemId, quantity, date, time, totalAmount);
    }
}
