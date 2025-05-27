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
    
    // Status Selection Enum
    public enum Status {
        ALL,
        ON_SALE,
        NOT_ON_SALE
    }

    //  Default Selection = ALL
    private Status status = Status.ALL;
    
    //   Column Names
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
    
    public void setStatus(Status status) {
        this.status = status;
        refresh();
    }

    public void refresh() {
        
        //  Dynamically render based on the user SELECTION
        switch (status) {
            case ON_SALE:
                itemTable = itemRepo.getAll().stream()
                        .filter(item -> item.getStatus() == Item.Status.onSale)
                        .toList();
                break;
            case NOT_ON_SALE:
                itemTable = itemRepo.getAll().stream()
                        .filter(item -> item.getStatus() == Item.Status.notOnSale)
                        .toList();
                break;
            default:
                itemTable = itemRepo.getAll().stream().toList();
                break;
        }

        fireTableDataChanged();
    }

    public void searchByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            refresh(); // fallback to status-based filtering
            return;
        }

        String keyword = name.trim().toLowerCase();

       itemTable = itemRepo.getAll().stream()
            .filter(item -> 
                item.getName().toLowerCase().contains(keyword) || 
                String.valueOf(item.getItemId()).toLowerCase().contains(keyword)
            )
            .toList();

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
