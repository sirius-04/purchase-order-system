/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpers;

import dtos.PurchaseOrderTableRow;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.Item;
import models.PurchaseOrder;
import repository.ItemRepository;
import repository.PurchaseOrdersRepository;
import services.TableManager;

/**
 *
 * @author ngoh
 */
public class PurchaseOrderTableHelper extends BaseTableHelper {
    public static void populatePurchaseOrder(DefaultTableModel model) {
        PurchaseOrdersRepository orderRepo = new PurchaseOrdersRepository();
        ItemRepository itemRepo = new ItemRepository();

        List<PurchaseOrder> purchaseOrders = orderRepo.getAll();
        List<PurchaseOrderTableRow> rows = new ArrayList<>();
        for (PurchaseOrder po: purchaseOrders) {
            try {
                Item item = itemRepo.find(po.getItemId());
                PurchaseOrderTableRow row = new PurchaseOrderTableRow(
                        po.getPurchaseOrderId(),
                        po.getItemId(),
                        item.getName(),
                        po.getQuantity(),
                        po.getPrice(),
                        po.getPurchaseManagerId(),
                        po.getStatus()
                );
                rows.add(row);
            } catch (Exception e) {
                System.err.println("Purchase Order not found: " + po.getItemId());
            }
        }

        if (rows.isEmpty()) {
            model.setRowCount(0);
            model.addRow(new Object[]{"No purchase order yet", "", "", "", "", "", ""});
        } else {
            TableManager.populateTable(model, rows, true);
        }
    }
}
