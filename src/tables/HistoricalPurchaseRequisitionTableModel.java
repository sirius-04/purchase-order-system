/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tables;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.Item;
import models.PurchaseRequisition;
import models.users.User;
import repository.ItemRepository;
import repository.PurchaseRequisitionRepository;
import repository.UserRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class HistoricalPurchaseRequisitionTableModel extends AbstractTableModel implements SearchableTableModel {

    private final String[] columns = {
        "Requisition ID",
        "Item ID",
        "Item Name",
        "Quantity",
        "Generated Date",
        "Required Date",
        "Supplier ID",
        "Sales Manager"
    };

    private final PurchaseRequisitionRepository requisitionRepo = new PurchaseRequisitionRepository();
    private final ItemRepository itemRepo = new ItemRepository();
    private final UserRepository salesManagerRepo = new UserRepository();

    private List<PurchaseRequisition> historicalRequisitions = new ArrayList<>();

    public HistoricalPurchaseRequisitionTableModel() {
        refresh();
    }

    public void refresh() {
        List<PurchaseRequisition> allRequisitions = requisitionRepo.getAll();
        historicalRequisitions = new ArrayList<>();

        for (PurchaseRequisition pr : allRequisitions) {
            if (pr.getStatus() != PurchaseRequisition.Status.pending) {
                historicalRequisitions.add(pr);
            }
        }

        fireTableDataChanged();
    }
    
    @Override
    public void filterByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            refresh();
            return;
        }
        
        String lowerKeyword = keyword.toLowerCase();
        

        historicalRequisitions = requisitionRepo.getAll().stream()
            .filter(pr -> {
                if (pr.getStatus() == PurchaseRequisition.Status.pending) return false;

                // PR ID
                boolean matchesRequisitionId = pr.getRequisitionId().toLowerCase().contains(lowerKeyword);

                // Item ID
                boolean matchesItemId = pr.getItemId().toLowerCase().contains(lowerKeyword);

                Item item = itemRepo.find(pr.getItemId());

                // Item Name
                boolean matchesItemName = item != null && item.getName().toLowerCase().contains(lowerKeyword);

                User sm = salesManagerRepo.find(pr.getSalesManagerId());

                // Sales Manager Name
                boolean matchesSalesManager = sm != null && sm.getUsername().toLowerCase().contains(lowerKeyword);

                return matchesRequisitionId || matchesItemId || matchesItemName || matchesSalesManager;
            })
            .toList();


        fireTableDataChanged();
    }   
           
    @Override
    public int getRowCount() {
        return historicalRequisitions.size();
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
        if (rowIndex < 0 || rowIndex >= historicalRequisitions.size()) {
            return null;
        }

        PurchaseRequisition pr = historicalRequisitions.get(rowIndex);
        Item item = itemRepo.find(pr.getItemId());
        User sm = salesManagerRepo.find(pr.getSalesManagerId());

        return switch (columnIndex) {
            case 0 ->
                pr.getRequisitionId();
            case 1 ->
                pr.getItemId();
            case 2 ->
                item != null ? item.getName() : "Unknown Item";
            case 3 ->
                pr.getQuantity();
            case 4 ->
                pr.getGeneratedDate();
            case 5 ->
                pr.getRequiredDate();
            case 6 ->
                item != null ? item.getSupplierId() : "Unknown Supplier";
            case 7 ->
                sm != null ? sm.getUsername() : "Unknown Manager";
            default ->
                null;
        };
    }

    public PurchaseRequisition getRequisitionAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= historicalRequisitions.size()) {
            return null;
        }
        return historicalRequisitions.get(rowIndex);
    }
}
