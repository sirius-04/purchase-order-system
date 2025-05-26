/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpers;

import dtos.PaymentTableRow;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import models.Payment;
import models.Supplier;
import repository.PaymentRepository;
import repository.PurchaseOrdersRepository;
import repository.SupplierRepository;
import utils.TableManager;

/**
 *
 * @author ngoh
 */
public class PaymentTableHelper extends BaseTableHelper {
    private static final TableManager<PaymentTableRow> tableManager = new TableManager<>();
    
    public static void populatePayment(JTable table) {  
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        PaymentRepository paymentRepo = new PaymentRepository();
        SupplierRepository supplierRepo = new SupplierRepository();
        PurchaseOrdersRepository purchaseOrderRepo = new PurchaseOrdersRepository();

        List<Payment> payment = paymentRepo.getAll();
        List<PaymentTableRow> rows = new ArrayList<>();
        for (Payment pay: payment) {
            try {
                var purchaseOrder = purchaseOrderRepo.find(pay.getPurchaseOrderId());
                String supplierId = purchaseOrder.getSupplierId();
                Supplier supplier = supplierRepo.find(supplierId);
                
                PaymentTableRow row = new PaymentTableRow(
                       supplier.getSupplierId(),
                       supplier.getName(),
                       supplier.getEmail(),
                       supplier.getContactNum(),
                       pay.getAmountPaid(),
                       pay.getStatus()
                );
                rows.add(row);
            } catch (Exception e) {
                System.err.println("Payment not found: " + pay.getPaymentId());
            }
        }

        if (rows.isEmpty()) {
            model.setRowCount(0);
            model.addRow(new Object[]{"No payment yet", "", "", "", "", "", ""});
        } else {
            tableManager.populateTable(model, rows, true);
        }
    }
}

