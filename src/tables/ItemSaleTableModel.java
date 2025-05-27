/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;
import models.Item;
import models.Sales;
import repository.ItemRepository;
import repository.SalesRepository;
import utils.DateTimeService;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemSaleTableModel extends AbstractTableModel {

    private final String[] columnNames = {
        "Item ID",
        "Item Name",
        "Quantity Sold",
        "Price Per Unit",
        "Total Amount"
    };

    private final List<Object[]> dataRows = new ArrayList<>();
    private final Map<Integer, Item> rowToItemMap = new HashMap<>();
    private final ItemRepository itemRepo = new ItemRepository();

    public ItemSaleTableModel() {
        loadData();
    }

    private void loadData() {
        dataRows.clear();
        rowToItemMap.clear();

        String today = DateTimeService.getCurrentDate();

        SalesRepository salesRepo = new SalesRepository();
        List<Sales> todaySales = salesRepo.getAll().stream()
                .filter(sale -> today.equals(sale.getDate()))
                .collect(Collectors.toList());

        Map<String, Integer> quantityMap = new HashMap<>();
        Map<String, Double> totalAmountMap = new HashMap<>();

        for (Sales sale : todaySales) {
            String itemId = sale.getItemId();
            quantityMap.put(itemId, quantityMap.getOrDefault(itemId, 0) + sale.getQuantity());
            totalAmountMap.put(itemId, totalAmountMap.getOrDefault(itemId, 0.0) + sale.getTotalAmount());
        }

        List<Item> allOnSaleItems = itemRepo.getAll().stream()
                .filter(item -> item.getStatus() == Item.Status.onSale)
                .collect(Collectors.toList());

        int rowIndex = 0;
        for (Item item : allOnSaleItems) {
            String itemId = item.getItemId();
            int quantitySold = quantityMap.getOrDefault(itemId, 0);
            double totalAmount = totalAmountMap.getOrDefault(itemId, 0.0);
            double pricePerUnit = item.getSellPrice();

            Object[] row = {
                itemId,
                item.getName(),
                quantitySold,
                pricePerUnit,
                totalAmount
            };

            dataRows.add(row);
            rowToItemMap.put(rowIndex++, item);
        }
    }

    @Override
    public int getRowCount() {
        return dataRows.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < dataRows.size()) {
            return dataRows.get(rowIndex)[columnIndex];
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 2 ->
                Integer.class;
            case 3, 4 ->
                Double.class;
            default ->
                String.class;
        };
    }

    public Item getItemAt(int rowIndex) {
        return rowToItemMap.getOrDefault(rowIndex, null);
    }

    public void refresh() {
        loadData();
        fireTableDataChanged();
    }
}
