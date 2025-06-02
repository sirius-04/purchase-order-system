package tables;

import java.util.*;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;
import models.Item;
import models.Sales;
import repository.ItemRepository;
import repository.SalesRepository;
import utils.DateTimeService;

public class ItemSaleTableModel extends AbstractTableModel implements SearchableTableModel {

    private final String[] columnNames = {
        "Item ID",
        "Item Name",
        "Quantity Sold",
        "Price Per Unit",
        "Total Amount"
    };

    private final List<Item> allItems = new ArrayList<>();
    private final List<Item> dataRows = new ArrayList<>();

    private final Map<String, Integer> quantityMap = new HashMap<>();
    private final Map<String, Double> totalAmountMap = new HashMap<>();
    private final Map<Integer, Item> rowToItemMap = new HashMap<>();

    private final ItemRepository itemRepo = new ItemRepository();
    private final SalesRepository salesRepo = new SalesRepository();

    private String currentDate = DateTimeService.getCurrentDate();

    public ItemSaleTableModel() {
        loadData();
    }

    private void loadData() {
        allItems.clear();
        dataRows.clear();
        rowToItemMap.clear();
        quantityMap.clear();
        totalAmountMap.clear();

        List<Sales> filteredSales = salesRepo.getAll().stream()
                .filter(sale -> currentDate.equals(sale.getDate()))
                .collect(Collectors.toList());

        for (Sales sale : filteredSales) {
            String itemId = sale.getItemId();
            quantityMap.put(itemId, quantityMap.getOrDefault(itemId, 0) + sale.getQuantity());
            totalAmountMap.put(itemId, totalAmountMap.getOrDefault(itemId, 0.0) + sale.getTotalAmount());
        }

        List<Item> items = itemRepo.getAll().stream()
                .filter(item -> item.getStatus() == Item.Status.onSale)
                .collect(Collectors.toList());

        allItems.addAll(items);
        dataRows.addAll(items);

        for (int i = 0; i < dataRows.size(); i++) {
            rowToItemMap.put(i, dataRows.get(i));
        }

        fireTableDataChanged();
    }

    public void setDate(String date) {
        this.currentDate = date;
        refresh();
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
        if (rowIndex < 0 || rowIndex >= dataRows.size()) {
            return null;
        }

        Item item = dataRows.get(rowIndex);
        String itemId = item.getItemId();

        return switch (columnIndex) {
            case 0 ->
                itemId;
            case 1 ->
                item.getName();
            case 2 ->
                quantityMap.getOrDefault(itemId, 0);
            case 3 ->
                item.getSellPrice();
            case 4 ->
                totalAmountMap.getOrDefault(itemId, 0.0);
            default ->
                null;
        };
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
    }

    @Override
    public void filterByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            dataRows.clear();
            dataRows.addAll(allItems);
        } else {
            String lowerKeyword = keyword.toLowerCase();
            dataRows.clear();
            dataRows.addAll(allItems.stream()
                    .filter(item -> item.getItemId().toLowerCase().contains(lowerKeyword)
                    || item.getName().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList()));
        }

        rowToItemMap.clear();
        for (int i = 0; i < dataRows.size(); i++) {
            rowToItemMap.put(i, dataRows.get(i));
        }

        fireTableDataChanged();
    }
}
