/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import models.Item;
import services.FileManager;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemRepository {
    private final String fileName = "items";
    FileManager fm = new FileManager();

    public void save(Item item) {
        String row = String.format("%s,%s,%d,%.2f,%s",
                item.getItemId(),
                item.getName(),
                item.getStockQuantity(),
                item.getPrice(),
                item.getSupplierId()
        );

        fm.writeFile(fileName, row);
    }


}
