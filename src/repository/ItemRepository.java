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
        String row = formatItemToRow(item);

        fm.writeFile(fileName, row);
    }

    public void update(Item updatedItem) {
        int targetItemRowNumber = findItemRowById(updatedItem.getItemId());

        String updatedRowContent = formatItemToRow(updatedItem);

        fm.editFile(fileName, targetItemRowNumber, updatedRowContent);
    }

    public Item find(String targetItemId) {
        String[] itemRow = getItemRows();
        
        for (String rowData : itemRow) {
            String[] columns = rowData.split(",");

            if (columns.length > 0 && columns[0].trim().equals(targetItemId)) {
                String itemId = columns[0].trim();
                String itemName = columns[1].trim();
                int stockNumber = Integer.parseInt(columns[2].trim());
                double price = Double.parseDouble(columns[3].trim());
                String supplierId = columns[4].trim();

                return new Item(itemId, itemName, stockNumber, price, supplierId);
            }
        }

        throw new IllegalArgumentException("Item not found: " + targetItemId);
    }

    public void delete(Item itemToRemove) {
        int targetItemRowNumber = findItemRowById(itemToRemove.getItemId());

        fm.removeRow(fileName, targetItemRowNumber);
    }

    private String[] getItemRows() {
        return fm.readFile(fileName);
    }

    private int findItemRowById(String targetItemId) {
        int currentRow = 1;

        String[] itemRow = getItemRows();
        
        for (String rowData : itemRow) {
            String[] columns = rowData.split(",");

            if (columns.length > 0 && columns[0].trim().equals(targetItemId)) {
                return currentRow;
            }

            currentRow++;
        }

        throw new IllegalArgumentException("Item row not found: " + targetItemId);
    }

    private String formatItemToRow(Item item) {
        String rowFormat = "%s,%s,%d,%.2f,%s";

        String formattedRow = String.format(rowFormat,
                item.getItemId(),
                item.getName(),
                item.getStockQuantity(),
                item.getPrice(),
                item.getSupplierId()
        );

        return formattedRow;
    }
}
