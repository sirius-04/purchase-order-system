/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tables;

import java.util.ArrayList;
import java.util.List;
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
public class DailySalesTableModel extends AbstractTableModel implements SearchableTableModel {

    private final String[] columnNames = {
        "Time", "Sale ID", "Item ID", "Item Name", "Quantity", "Price Per Unit", "Amount"
    };

    private  List<Sales> salesList = new ArrayList<>();
    private final ItemRepository itemRepo = new ItemRepository();

    public DailySalesTableModel() {
        loadData();
    }

    private void loadData() {
        SalesRepository salesRepo = new SalesRepository();
        String today = DateTimeService.getCurrentDate();

        List<Sales> todaysSales = salesRepo.getAll().stream()
                .filter(sale -> today.equals(sale.getDate()))
                .toList();

        salesList.clear();
        salesList.addAll(todaysSales);
    }

    @Override
    public int getRowCount() {
        return salesList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Sales sale = salesList.get(rowIndex);

        try {
            Item item = itemRepo.find(sale.getItemId());

            return switch (columnIndex) {
                case 0 ->
                    sale.getTime();
                case 1 ->
                    sale.getSalesId();
                case 2 ->
                    sale.getItemId();
                case 3 ->
                    item.getName();
                case 4 ->
                    sale.getQuantity();
                case 5 ->
                    item.getSellPrice();
                case 6 ->
                    sale.getTotalAmount();
                default ->
                    null;
            };

        } catch (Exception e) {
            System.err.println("Error loading item data for sale ID " + sale.getSalesId() + ": " + e.getMessage());
            return "N/A";
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 4 ->
                Integer.class;
            case 5, 6 ->
                Double.class;
            default ->
                String.class;
        };
    }

    public Sales getSalesAt(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < salesList.size()) {
            return salesList.get(rowIndex);
        }
        return null;
    }

    public void removeRow(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < salesList.size()) {
            salesList.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public void refresh() {
        loadData();
        fireTableDataChanged();
    }
    
    @Override
    public void filterByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            refresh();
            return;
        }

        SalesRepository salesRepo = new SalesRepository();
        String today = DateTimeService.getCurrentDate();
        
        String lowerKeyword = keyword.toLowerCase();

        List<Sales> todaysSales = salesRepo.getAll().stream()
                .filter(sale -> today.equals(sale.getDate()) 
                        && sale.getSalesId().toLowerCase().contains(lowerKeyword))
                .toList();
        
        salesList.clear();
        salesList.addAll(todaysSales);
        
        fireTableDataChanged();
    }
}
