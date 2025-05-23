/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpers;

import dtos.PurchaseRequisitionTableRow;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.PurchaseRequisition;
import repository.PurchaseRequisitionRepository;
import models.Item;
import repository.ItemRepository;
import repository.UserRepository;
/**
 *
 * @author Acer
 */
public class PurchaseRequisitionsTableHelper {

    public static void populateAllRequisitions(DefaultTableModel pendingModel, DefaultTableModel historicalModel) {
        PurchaseRequisitionRepository prRepo = new PurchaseRequisitionRepository();
        ItemRepository itemRepo = new ItemRepository();
        UserRepository smRepo = new UserRepository(); 

        List<PurchaseRequisition> requisitions = prRepo.getAll();

        List<Object[]> pendingRows = new ArrayList<>();
        List<Object[]> historicalRows = new ArrayList<>();
        List<PurchaseRequisitionTableRow> rows = new ArrayList<>();
        for (PurchaseRequisition pr : requisitions) {
            try {
                Item item = itemRepo.find(pr.getItemId());
                String itemName = item.getName();
                String supplierId = item.getSupplierId();
                String salesManagerName = smRepo.find(pr.getSalesManagerId()).getUsername(); 

                PurchaseRequisitionTableRow row = new PurchaseRequisitionTableRow(
                        pr.getRequisitionId(),
                        pr.getItemId(),
                        itemName,
                        pr.getQuantity(),
                        pr.getGeneratedDate(),
                        pr.getRequiredDate(),
                        supplierId,
                        salesManagerName
                );

                if (pr.getStatus() == PurchaseRequisition.Status.pending) {
                    pendingRows.add(row.toTableRow());
                } else {
                    historicalRows.add(row.toTableRow());
                }

            } catch (Exception e) {
                System.err.println("Error processing requisition ID: " + pr.getRequisitionId() + " | " + e.getMessage());
            }
        }

        fillTable(pendingModel, pendingRows);
        fillTable(historicalModel, historicalRows);
    }

    private static void fillTable(DefaultTableModel model, List<Object[]> rows) {
        model.setRowCount(0);
        if (rows.isEmpty()) {
            model.addRow(new Object[]{"No purchase requisitions yet", "", "", "", "", "", "", ""});
        } else {
            for (Object[] row : rows) {
                model.addRow(row);
            }
        }
    }
}

