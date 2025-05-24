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

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class SupplierTableHelper {
    
    public static void populateSupplier(DefaultTableModel model) {
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
            TableManager.populateTable(model, rows, true);
        }
    }
}
