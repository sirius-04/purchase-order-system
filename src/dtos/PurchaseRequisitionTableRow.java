/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;
import models.PurchaseRequisition;

/**
 *
 * @author Acer
 */
public class PurchaseRequisitionTableRow implements TableConvertible {
    private String requisitionId;
    private String itemId;
    private String itemName;
    private int quantity;
    private String generatedDate;
    private String requiredDate;
    private String supplierId;
    private String salesManagerName;

    public PurchaseRequisitionTableRow(
            String requisitionId,
            String itemId,
            String itemName,
            int quantity,
            String generatedDate,
            String requiredDate,
            String supplierId,
            String salesManagerName
    ) {
        this.requisitionId = requisitionId;
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.generatedDate = generatedDate;
        this.requiredDate = requiredDate;
        this.supplierId = supplierId;
        this.salesManagerName = salesManagerName;
    }

    @Override
    public Object[] toTableRow() {
        return new Object[]{
            requisitionId,
            itemId,
            itemName,
            quantity,
            generatedDate,
            requiredDate,
            supplierId,
            salesManagerName
        };
    }
}
