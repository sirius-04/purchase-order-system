/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import models.PurchaseRequisition;
/**
 *
 * @author dede
 */
public class PurchaseRequisitionRepository extends BaseRepository<PurchaseRequisition>{
    
     public PurchaseRequisitionRepository() {
        super("purchase_requisitions.txt", "%s,%s,%s,%d,%s,%s,%s");
    }
    
     @Override
    protected String formatToRow(PurchaseRequisition purchaseRequisition) {
        return String.format(rowFormat,
                purchaseRequisition.getRequisitionId(),
                purchaseRequisition.getSalesManagerId(),
                purchaseRequisition.getItemId(),
                purchaseRequisition.getQuantity(),
                purchaseRequisition.getGeneratedDate(),
                purchaseRequisition.getRequiredDate(),
                purchaseRequisition.getStatus()
        );
    }

    @Override
    protected PurchaseRequisition parseRow(String[] columns) {
        String requisitionId = columns[0].trim();
        String salesManagerId = columns[1].trim();
        String itemId = columns[2].trim();
        int quantity = Integer.parseInt(columns[3].trim());
        String generatedDate = columns[4].trim();
        String requiredDate = columns[5].trim();
        PurchaseRequisition.Status status = PurchaseRequisition.Status.valueOf(columns[6].trim());

        return new PurchaseRequisition(requisitionId, salesManagerId, itemId, quantity, generatedDate, requiredDate, status);
    }
    
}
