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
import repository.SupplierRepository;
import utils.TableManager;

/**
 *
 * @author ngoh
 */
public class PaymentTableHelper extends BaseTableHelper {
    public static void populatePayment(JTable table) {  
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        PaymentRepository paymentRepo = new PaymentRepository();
        SupplierRepository supplierRepo = new SupplierRepository();

        List<Payment> payment = paymentRepo.getAll();
        List<PaymentTableRow> rows = new ArrayList<>();
        for (Payment pay: payment) {
            try {
                Supplier supplier = supplierRepo.find(pay.getPaymentId());
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
            TableManager.populateTable(model, rows, true);
        }
    }
}

