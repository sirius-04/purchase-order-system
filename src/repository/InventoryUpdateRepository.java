/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import models.InventoryUpdate;
import models.InventoryUpdate.Status;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryUpdateRepository extends BaseRepository<InventoryUpdate> {

    public InventoryUpdateRepository() {
        super("inventory_update", "%s,%s,%s,%d,%.2f,%s");
    }

    @Override
    protected String formatToRow(InventoryUpdate inventoryUpdate) {
        return String.format(rowFormat,
                inventoryUpdate.getInventoryUpdateId(),
                inventoryUpdate.getItemId(),
                inventoryUpdate.getSupplierId(),
                inventoryUpdate.getUpdateQuantity(),
                inventoryUpdate.getTotalAmount(),
                inventoryUpdate.getStatus()
        );
    }

    @Override
    protected InventoryUpdate parseRow(String[] columns) {
        String inventoryUpdateId = columns[0].trim();
        String itemId = columns[1].trim();
        String supplierId = columns[2].trim();
        int updateQuantity = Integer.parseInt(columns[3].trim());
        double totalAmount = Double.parseDouble(columns[4].trim());
        Status status = Status.valueOf(columns[5].trim());

        return new InventoryUpdate(inventoryUpdateId, itemId, supplierId, updateQuantity, totalAmount, status);
    }
}
