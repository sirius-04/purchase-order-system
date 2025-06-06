/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import models.PurchaseOrder;

/**
 *
 * @author dede
 */
public class PurchaseOrdersRepository extends BaseRepository<PurchaseOrder>  {
    
    public PurchaseOrdersRepository() {
        super("purchase_orders", "%s,%s,%s,%s,%d,%.2f,%s,%s,%s");
    }
    
     @Override
    protected String formatToRow(PurchaseOrder purchaseOrder) {
        return String.format(rowFormat,
                purchaseOrder.getPurchaseOrderId(),
                purchaseOrder.getItemId(),
                purchaseOrder.getPurchaseRequisitionId(),
                purchaseOrder.getUserId(),
                purchaseOrder.getQuantity(),
                purchaseOrder.getPrice(),
                purchaseOrder.getDate(),
                purchaseOrder.getStatus(),
                purchaseOrder.getSupplierId()
        );
    }

    @Override
    protected PurchaseOrder parseRow(String[] columns) {
        String purchaseOrderId = columns[0].trim();
        String itemId = columns[1].trim();
        String purchaseRequisitionId = columns[2].trim();
        String userId = columns[3].trim();
        int quantity = Integer.parseInt(columns[4].trim());
        double price = Double.parseDouble(columns[5].trim());
        String date = columns[6].trim();
        PurchaseOrder.Status status = PurchaseOrder.Status.valueOf(columns[7].trim());
        String supplierId = columns[8].trim();

        return new PurchaseOrder(purchaseOrderId, itemId, purchaseRequisitionId, userId, quantity, price, date, status, supplierId);
    }
}