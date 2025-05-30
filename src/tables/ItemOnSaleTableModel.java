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
import repository.ItemSupplierRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemOnSaleTableModel extends AbstractTableModel implements SearchableTableModel {

    private final String[] columnNames = {
        "Item ID",
        "Item Name",
        "Cost Price",
        "Sell Price",
        "Stock Quantity",
        "Supplier Name"
    };

    private final ItemRepository itemRepo = new ItemRepository();
    private final ItemSupplierRepository itemSupplierRepo = new ItemSupplierRepository();

    private List<Item> itemsOnSale = new ArrayList<>();

    public ItemOnSaleTableModel() {
        refresh();
    }

    public void refresh() {
        itemsOnSale = itemRepo.getAll().stream()
                .filter(item -> item.getStatus() == Item.Status.onSale)
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

        itemsOnSale = itemRepo.getAll().stream()
                .filter(item -> item.getStatus() == Item.Status.onSale
                && (item.getName().toLowerCase().contains(lowerKeyword)
                || item.getItemId().toLowerCase().contains(lowerKeyword)))
                .toList();

        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return itemsOnSale.size();
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
        if (rowIndex < 0 || rowIndex >= itemsOnSale.size()) {
            return null;
        }

        Item item = itemsOnSale.get(rowIndex);

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
                List<Supplier> supplierList = itemSupplierRepo.getItemSupplier(item.getItemId());

                if (supplierList.isEmpty()) {
                    return "supplier not found";
                }

                StringBuilder suppliersBuilder = new StringBuilder();
                for (Supplier supplier : supplierList) {
                    suppliersBuilder.append(supplier.getName()).append(", ");
                }

                String suppliers = suppliersBuilder.toString();
                if (suppliers.endsWith(", ")) {
                    suppliers = suppliers.substring(0, suppliers.length() - 2);
                }

                return suppliers;

            default:
                return null;
        }
    }

    public Item getItemAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= itemsOnSale.size()) {
            return null;
        }
        return itemsOnSale.get(rowIndex);
    }
}
