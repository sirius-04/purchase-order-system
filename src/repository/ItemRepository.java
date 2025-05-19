/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import models.Item;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemRepository extends BaseRepository<Item> {

    public ItemRepository() {
        super("item.txt", "%s,%s,%d,%d,%.2f,%s,%s");
    }

    @Override
    protected String formatToRow(Item item) {
        return String.format(rowFormat,
                item.getItemId(),
                item.getName(),
                item.getStockQuantity(),
                item.getPrice(),
                item.getSellPrice(),
                item.getSupplierId(),
                item.getStatus()
        );
    }

    @Override
    protected Item parseRow(String[] columns) {
        String itemId = columns[0].trim();
        String itemName = columns[1].trim();
        int stockNumber = Integer.parseInt(columns[2].trim());
        double price = Double.parseDouble(columns[3].trim());
        double sellPrice = Double.parseDouble(columns[4].trim());
        String supplierId = columns[5].trim();
        Item.Status status = Item.Status.valueOf(columns[6].trim());

        return new Item(itemId, itemName, stockNumber, price, sellPrice, supplierId, status);
    }
}
