/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tables;

/**
 *
 * @author dede
 */

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.Item;
import models.Supplier;
import repository.ItemRepository;
import repository.SupplierRepository;

public class ItemTableModel extends AbstractTableModel  {
    private final String[] columnNames = {
        "Item ID",
        "Item Name",
        "Price",
        "Stock Quantity",
        "Supplier Name"
    };

    private final ItemRepository itemRepo = new ItemRepository();
    private final SupplierRepository supplierRepo = new SupplierRepository();

    private List<Item> itemTable = new ArrayList<>();

    public ItemTableModel() {
        refresh();
    }

    public void refresh() {
        itemTable = itemRepo.getAll().stream().toList();

        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return itemTable.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex >= 0 && columnIndex < columnNames.length) {
            return columnNames[columnIndex];
        }
        return super.getColumnName(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= itemTable.size()) {
            return null;
        }

        Item item = itemTable.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return item.getId();

            case 1:
                return item.getName();

            case 2:
                return item.getPrice();

            case 3:
                return item.getStockQuantity();

            case 4:
                Supplier supplier = supplierRepo.find(item.getSupplierId());
                return supplier != null ? supplier.getName() : "Unknown";

            default:
                return null;
        }
    }

    public Item getItemAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= itemTable.size()) {
            return null;
        }
        return itemTable.get(rowIndex);
    }
}
