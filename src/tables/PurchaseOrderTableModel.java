/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tables;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.table.AbstractTableModel;
import models.Item;
import models.PurchaseOrder;
import repository.ItemRepository;
import repository.PurchaseOrdersRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class PurchaseOrderTableModel extends AbstractTableModel {

   private final String[] allColumns = {
        "Order ID",       
        "Item ID",        
        "Item Name",      
        "Quantity",       
        "Price",          
        "Purchase Manager ID", 
        "Status",         
        "Supplier ID"     
    };

    private final int[] defaultColumnIndexes = { 0, 1, 2, 3, 4, 5, 6, 7 };

    private int[] visibleColumnIndexes;

    private final PurchaseOrdersRepository orderRepo = new PurchaseOrdersRepository();
    private final ItemRepository itemRepo = new ItemRepository();

    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();
    private final PurchaseOrder.Status statusFilter;

    public PurchaseOrderTableModel(PurchaseOrder.Status statusFilter, boolean showStatusColumn) {
        this.statusFilter = statusFilter;
        this.visibleColumnIndexes = showStatusColumn
            ? defaultColumnIndexes
            : new int[] { 0, 1, 2, 3, 4, 5, 7 }; // omit index 6 ("Status")
        refresh();
    }

    public void refresh() {
        List<PurchaseOrder> allOrders = orderRepo.getAll();
        this.purchaseOrders = allOrders.stream()
            .filter(po -> po.getStatus() == statusFilter)
            .collect(Collectors.toList());
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return purchaseOrders.size();
    }

    @Override
    public int getColumnCount() {
        return visibleColumnIndexes.length;
    }

    @Override
    public String getColumnName(int column) {
        int actualIndex = visibleColumnIndexes[column];
        return allColumns[actualIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= purchaseOrders.size()) {
            return null;
        }

        PurchaseOrder po = purchaseOrders.get(rowIndex);
        Item item = itemRepo.find(po.getItemId());
        int actualIndex = visibleColumnIndexes[columnIndex];

        return switch (actualIndex) {
            case 0 -> po.getPurchaseOrderId();
            case 1 -> po.getItemId();
            case 2 -> item != null ? item.getName() : "Unknown Item";
            case 3 -> po.getQuantity();
            case 4 -> po.getPrice();
            case 5 -> po.getPurchaseManagerId();
            case 6 -> po.getStatus();
            case 7 -> po.getSupplierId();
            default -> null;
        };
    }

    public PurchaseOrder getPurchaseOrderAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= purchaseOrders.size()) {
            return null;
        }
        return purchaseOrders.get(rowIndex);
    }
}
