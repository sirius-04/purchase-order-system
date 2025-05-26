/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tables;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.Supplier;
import repository.SupplierRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class SupplierTableModel extends AbstractTableModel {

    private final String[] columns = {
        "Supplier ID",
        "Name",
        "Email",
        "Contact Number"
    };

    private final SupplierRepository supplierRepo = new SupplierRepository();
    private List<Supplier> suppliers = new ArrayList<>();

    public SupplierTableModel() {
        refresh();
    }

    public void refresh() {
        suppliers = supplierRepo.getAll();
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return suppliers.size();
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
        if (rowIndex < 0 || rowIndex >= suppliers.size()) {
            return null;
        }

        Supplier sup = suppliers.get(rowIndex);

        return switch (columnIndex) {
            case 0 ->
                sup.getSupplierId();
            case 1 ->
                sup.getName();
            case 2 ->
                sup.getEmail();
            case 3 ->
                sup.getContactNum();
            default ->
                null;
        };
    }

    public Supplier getSupplierAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= suppliers.size()) {
            return null;
        }
        return suppliers.get(rowIndex);
    }
}
