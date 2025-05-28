/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tables;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.InventoryUpdate;
import models.Item;
import repository.InventoryUpdateRepository;
import repository.ItemRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryUpdateTableModel extends AbstractTableModel {

    private final String[] columns = {
        "Inventory Update ID",
        "Item ID",
        "Item Name",
        "Supplier ID",
        "Update Quantity",
        "Total Amount"
    };

    private final InventoryUpdateRepository inventoryRepo = new InventoryUpdateRepository();
    private final ItemRepository itemRepo = new ItemRepository();
    private List<InventoryUpdate> inventoryUpdates = new ArrayList<>();

    public InventoryUpdateTableModel() {
        refresh();
    }

    public void refresh() {
        this.inventoryUpdates = inventoryRepo.getAll();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return inventoryUpdates.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return (column >= 0 && column < columns.length) ? columns[column] : super.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= inventoryUpdates.size()) {
            return null;
        }

        InventoryUpdate inv = inventoryUpdates.get(rowIndex);
        Item item = itemRepo.find(inv.getItemId());

        return switch (columnIndex) {
            case 0 ->
                inv.getInventoryUpdateId();
            case 1 ->
                inv.getItemId();
            case 2 ->
                item != null ? item.getName() : "Unknown Item";
            case 3 ->
                inv.getSupplierId();
            case 4 ->
                inv.getUpdateQuantity();
            case 5 ->
                inv.getTotalAmount();
            default ->
                null;
        };
    }

    public InventoryUpdate getInventoryUpdateAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= inventoryUpdates.size()) {
            return null;
        }
        return inventoryUpdates.get(rowIndex);
    }
}
