/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tables;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.Item;
import models.Supplier;
import repository.ItemRepository;
import repository.SupplierRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemNotOnSaleTableModel extends AbstractTableModel implements SearchableTableModel {

    private final String[] columnNames = {
        "Item ID",
        "Item Name",
        "Cost Price",
        "Sell Price",
        "Stock Quantity",
        "Supplier Name"
    };

    private final ItemRepository itemRepo = new ItemRepository();
    private final SupplierRepository supplierRepo = new SupplierRepository();

    private List<Item> itemsNotOnSale = new ArrayList<>();

    public ItemNotOnSaleTableModel() {
        refresh();
    }

    public void refresh() {
        itemsNotOnSale = itemRepo.getAll().stream()
                .filter(item -> item.getStatus() == Item.Status.notOnSale)
                .toList();

        fireTableDataChanged();
    }
    
    @Override
    public void filterByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            refresh();
            return;
        }

        String lowerKeyword = keyword.toLowerCase();

        itemsNotOnSale = itemRepo.getAll().stream()
            .filter(item -> item.getStatus() == Item.Status.notOnSale &&
                    (item.getName().toLowerCase().contains(lowerKeyword) ||
                     item.getItemId().toLowerCase().contains(lowerKeyword)))
            .toList();

        fireTableDataChanged();
    }
    
    @Override
    public int getRowCount() {
        return itemsNotOnSale.size();
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
        if (rowIndex < 0 || rowIndex >= itemsNotOnSale.size()) {
            return null;
        }

        Item item = itemsNotOnSale.get(rowIndex);

        switch (columnIndex) {
            case 0:
                return item.getId();

            case 1:
                return item.getName();
                
            case 2:
                return item.getPrice();
                
            case 3:
                return item.getSellPrice();

            case 4:
                return item.getStockQuantity();

            case 5:
                Supplier supplier = supplierRepo.find(item.getSupplierId());
                return supplier != null ? supplier.getName() : "Unknown";

            default:
                return null;
        }
    }

    public Item getItemAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= itemsNotOnSale.size()) {
            return null;
        }
        return itemsNotOnSale.get(rowIndex);
    }
}
