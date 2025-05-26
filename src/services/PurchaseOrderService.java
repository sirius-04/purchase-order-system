/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import javax.swing.JTable;
import repository.PurchaseOrdersRepository;
import utils.IdGenerator;

/**
 *
 * @author ngoh
 */
public class PurchaseOrderService {
    private final IdGenerator idGenerator = new IdGenerator();
    private final PurchaseOrdersRepository purchaseOrderRepo = new PurchaseOrdersRepository();
    
    private int getColumnIndex(JTable table, String columnName) {
    for (int i = 0; i < table.getColumnCount(); i++) {
        if (table.getColumnName(i).equalsIgnoreCase(columnName)) {
            return i;
        }
    }
    return -1;
}
    
     public boolean approvePo(String poId) {
//        return purchaseOrderRepo.updateStatus(poId, "fulfilled");
          return true;
    }
    
    public void addApprovalListener(Component parent, JTable purchaseOrderTable) {
    purchaseOrderTable.addMouseListener(new java.awt.event.MouseAdapter() {
        @Override
        public void mouseClicked(java.awt.event.MouseEvent evt) {
            int row = purchaseOrderTable.getSelectedRow();
            if (row != -1) {
                int option = javax.swing.JOptionPane.showConfirmDialog(
                        parent,
                        "Do you want to approve this purchase order?",
                        "Approve Purchase Order",
                        javax.swing.JOptionPane.YES_NO_OPTION
                );

                if (option == javax.swing.JOptionPane.YES_OPTION) {
                    String poId = purchaseOrderTable.getValueAt(row, getColumnIndex(purchaseOrderTable, "Purchase Order ID")).toString();
                    boolean success = approvePo(poId);

                    if (success) {
                        purchaseOrderTable.setValueAt("fulfilled", row, getColumnIndex(purchaseOrderTable, "Status"));
                        javax.swing.JOptionPane.showMessageDialog(
                                parent,
                                "Approved successfully!",
                                "Success",
                                javax.swing.JOptionPane.INFORMATION_MESSAGE
                        );

                        ((javax.swing.table.DefaultTableModel) purchaseOrderTable.getModel()).removeRow(row);
                    } else {
                        javax.swing.JOptionPane.showMessageDialog(
                                parent,
                                "Failed to approve. Please try again.",
                                "Error",
                                javax.swing.JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            }
        }
    });
}

    public boolean verifyInventoryUpdate(Component parent, JTable itemTable) {
        return true;
    }
    
    public boolean processPayment (Component parent, JTable paymentTable) {
        return true;
    }
}
