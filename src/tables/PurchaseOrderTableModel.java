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


// Example for use this class, need to pass a parameter fo this class.
// PurchaseOrderTableModel purchaseOrderTableModel = new PurchaseOrderTableModel(PurchaseOrderTableModel.POStatus.FULFILLED)

public class PurchaseOrderTableModel extends AbstractTableModel implements SearchableTableModel{

    private final String[] allColumns = {
        "Order ID",
        "Item ID",
        "Item Name",
        "Quantity",
        "Price",
        "Raised User",
        "Status",
        "Supplier ID"
    };

    private final int[] allColumnsIndexes = { 0, 1, 2, 3, 4, 5, 6, 7 };
    private final int[] filteredColumnsIndexes = { 0, 1, 2, 3, 4, 5, 7 }; // remove the status column

    private int[] visibleColumnIndexes;

    private final PurchaseOrdersRepository orderRepo = new PurchaseOrdersRepository();
    private final ItemRepository itemRepo = new ItemRepository();

    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();
    private final POStatus statusFilter;

   public static enum POStatus {
    ALL,
    PENDING,
    APPROVED,
    VERIFIED,
    DELETED;

    public static POStatus fromString(String value) {
        try {
            return POStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return POStatus.ALL;
        }
    }
}

    
    public PurchaseOrderTableModel(POStatus statusFilter) {
        this.statusFilter = statusFilter;

        // Set visible columns based on filter
        this.visibleColumnIndexes = (statusFilter == POStatus.ALL)
            ? allColumnsIndexes
            : filteredColumnsIndexes;

        refresh();
    }

    public void refresh() {
        List<PurchaseOrder> allOrders = orderRepo.getAll();

        if (statusFilter == POStatus.ALL) {
            this.purchaseOrders = allOrders.stream()
                .filter(po -> po.getStatus() != PurchaseOrder.Status.deleted)
                .collect(Collectors.toList());
        } else if (statusFilter == POStatus.DELETED) {
            this.purchaseOrders = allOrders.stream()
                .filter(po -> po.getStatus() == PurchaseOrder.Status.deleted)
                .collect(Collectors.toList());
        } else {
            PurchaseOrder.Status status = switch (statusFilter) {
                case PENDING -> PurchaseOrder.Status.pending;
                case APPROVED -> PurchaseOrder.Status.approved;
                case VERIFIED -> PurchaseOrder.Status.verified;
                default -> null;
            };
            this.purchaseOrders = allOrders.stream()
                .filter(po -> po.getStatus() == status)
                .collect(Collectors.toList());
        }

        fireTableDataChanged();
    }


    public void filterByKeyword(String keyword) {
        List<PurchaseOrder> allOrders = orderRepo.getAll();

        // Step 1: Filter by statusFilter first (same as refresh logic)
        List<PurchaseOrder> filteredByStatus;
        if (statusFilter == POStatus.ALL) {
            filteredByStatus = allOrders.stream()
                .filter(po -> po.getStatus() != PurchaseOrder.Status.deleted)
                .collect(Collectors.toList());
        } else if (statusFilter == POStatus.DELETED) {
            filteredByStatus = allOrders.stream()
                .filter(po -> po.getStatus() == PurchaseOrder.Status.deleted)
                .collect(Collectors.toList());
        } else {
            PurchaseOrder.Status poStatus = switch (statusFilter) {
                case PENDING -> PurchaseOrder.Status.pending;
                case APPROVED -> PurchaseOrder.Status.approved;
                case VERIFIED -> PurchaseOrder.Status.verified;
                default -> null;
            };
            filteredByStatus = allOrders.stream()
                .filter(po -> po.getStatus() == poStatus)
                .collect(Collectors.toList());
        }

        // Step 2: If keyword is null or empty, just use filteredByStatus list
        if (keyword == null || keyword.trim().isEmpty()) {
            this.purchaseOrders = filteredByStatus;
            fireTableDataChanged();
            return;
        }

        String lowerKeyword = keyword.toLowerCase();

        // Step 3: Filter filteredByStatus list by keyword on PO id, item id, and item name
        this.purchaseOrders = filteredByStatus.stream()
            .filter(po -> {
                // PO ID
                boolean matchesPOId = po.getPurchaseOrderId().toLowerCase().contains(lowerKeyword);

                // Item ID
                boolean matchesItemId = po.getItemId().toLowerCase().contains(lowerKeyword);

                // Item Name (need to query itemRepo)
                Item item = itemRepo.find(po.getItemId());
                boolean matchesItemName = item != null && item.getName().toLowerCase().contains(lowerKeyword);

                return matchesPOId || matchesItemId || matchesItemName;
            })
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
            case 5 -> po.getUserId();
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


// Status enum
enum POStatus {
    ALL,
    PENDING,
    APPROVED,
    VERIFIED,
    DELETED;

    public static POStatus fromString(String value) {
        try {
            return POStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return POStatus.ALL;
        }
    }
}
