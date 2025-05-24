/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpers;

/**
 *
 * @author dede
 */
import dtos.SupplierTableRow;
import models.Supplier;
import repository.SupplierRepository;
import utils.TableManager;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SupplierTableHelper {
        private static final TableManager<SupplierTableRow> tableManager = new TableManager<>();
                
                
    public static void populateSupplier(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        SupplierRepository supplierRepo = new SupplierRepository();


        List<Supplier> supplier = supplierRepo.getAll().stream()
                .toList();

        List<SupplierTableRow> rows = new ArrayList<>();
        for (Supplier sup : supplier) {
            try {
                SupplierTableRow row = new SupplierTableRow(
                        sup.getSupplierId(),
                        sup.getName(),
                        sup.getEmail(),
                        sup.getContactNum()
                );
                rows.add(row);
            } catch (Exception e) {
                System.err.println("Supplier not found: " + sup.getSupplierId());
            }
        }

        if (rows.isEmpty()) {
            model.setRowCount(0);
            model.addRow(new Object[]{"No supplier yet", "", "", "", ""});
        } else {
            tableManager.populateTable(model, rows, true);
        }
    }
}
