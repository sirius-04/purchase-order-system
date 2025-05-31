/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tables;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.Payment;
import models.Supplier;
import repository.InventoryUpdateRepository;
import repository.PaymentRepository;
import repository.SupplierRepository;

/**
 *
 * @author Chan Yong Liang
 */
public class PaymentTableModel extends AbstractTableModel {

    private final String[] columns = {
        "Supplier ID",
        "Supplier Name",
        "Supplier Email",
        "Supplier Contact Number",
        "Balance",
        "Payment Status",
    };

    private final PaymentRepository paymentRepo = new PaymentRepository();
    private final SupplierRepository supplierRepo = new SupplierRepository();
    private final InventoryUpdateRepository inventoryUpdateRepo = new InventoryUpdateRepository();

    private List<Payment> payments = new ArrayList<>();

    public PaymentTableModel() {
        refresh();
    }

    public void refresh() {
        payments = paymentRepo.getAll();
        
        this.payments.sort((newPayment, oldPayment) -> {
            if (newPayment.getStatus() == oldPayment.getStatus()) {
                return 0;
            }
            return newPayment.getStatus() == Payment.Status.pending ? -1 : 1;
        });
        
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return payments.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        if (column >= 0 && column < columns.length) {
            return columns[column];
        }
        return super.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= payments.size()) {
            return null;
        }

        Payment pay = payments.get(rowIndex);

        // Find associated PurchaseOrder and Supplier
        var inventoryUpdate = inventoryUpdateRepo.find(pay.getInventoryUpdateId());
        if (inventoryUpdate == null) {
            return null;
        }

        Supplier supplier = supplierRepo.find(inventoryUpdate.getSupplierId());
        if (supplier == null) {
            return null;
        }

        return switch (columnIndex) {
            case 0 ->
                supplier.getSupplierId();
            case 1 ->
                supplier.getName();
            case 2 ->
                supplier.getEmail();
            case 3 ->
                supplier.getContactNum();
            case 4 ->
                pay.getAmountPaid();
            case 5 ->
                pay.getStatus();
            default ->
                null;
        };
    }

    public Payment getPaymentAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= payments.size()) {
            return null;
        }
        return payments.get(rowIndex);
    }
}