/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tables;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.Item;
import models.PurchaseOrder;
import repository.ItemRepository;
import repository.PurchaseOrdersRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryTableModel extends AbstractTableModel {

    private final String[] columns = {
        "Order ID",
        "Item ID",
        "Item Name",
        "Quantity",
        "Price",
        "Purchase Manager ID",
        "Status",
        "Supplier ID"
    };

    private final PurchaseOrdersRepository orderRepo = new PurchaseOrdersRepository();
    private final ItemRepository itemRepo = new ItemRepository();

    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();

    public InventoryTableModel() {
        refresh();
    }

    public void refresh() {
        List<PurchaseOrder> allOrders = orderRepo.getAll();
        List<PurchaseOrder> filtered = new ArrayList<>();

        for (PurchaseOrder po : allOrders) {
            if (po.getStatus() == PurchaseOrder.Status.fulfilled) {
                filtered.add(po);
            }
        }

        this.purchaseOrders = filtered;
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return purchaseOrders.size();
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
        if (rowIndex < 0 || rowIndex >= purchaseOrders.size()) {
            return null;
        }

        PurchaseOrder po = purchaseOrders.get(rowIndex);
        Item item = itemRepo.find(po.getItemId());

        return switch (columnIndex) {
            case 0 ->
                po.getPurchaseOrderId();
            case 1 ->
                po.getItemId();
            case 2 ->
                item != null ? item.getName() : "Unknown Item";
            case 3 ->
                po.getQuantity();
            case 4 ->
                po.getPrice();
            case 5 ->
                po.getPurchaseManagerId();
            case 6 ->
                po.getStatus();
            case 7 ->
                po.getSupplierId();
            default ->
                null;
        };
    }

    public PurchaseOrder getPurchaseOrderAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= purchaseOrders.size()) {
            return null;
        }
        return purchaseOrders.get(rowIndex);
    }
}
