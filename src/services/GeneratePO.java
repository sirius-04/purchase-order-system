/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Acer
 */
import models.PurchaseOrder;
import models.PurchaseRequisition;
import repository.PurchaseOrdersRepository;
import repository.PurchaseRequisitionRepository;
import utils.IdGenerator;
import repository.ItemRepository;
import repository.UserRepository;
import models.Item;
import models.users.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class GeneratePO {

    private final PurchaseRequisitionRepository requisitionRepo = new PurchaseRequisitionRepository();
    private final PurchaseOrdersRepository orderRepo = new PurchaseOrdersRepository();
    private final IdGenerator idGenerator = new IdGenerator();
    private final UserRepository userRepository = new UserRepository();
    private final ItemRepository itemRepository = new ItemRepository();

    public void attachRowClickListener(JTable pendingRequisitionTable,Runnable onGenerateCallback) {
        pendingRequisitionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = pendingRequisitionTable.getSelectedRow();
                if (selectedRow != -1 && e.getClickCount() == 1) {
                    String requisitionId = pendingRequisitionTable.getValueAt(selectedRow, 0).toString();
                    showPODialog(pendingRequisitionTable, requisitionId, onGenerateCallback);
                }
            }
        });
    }

    private void showPODialog(Component parent, String requisitionId,Runnable onGenerateCallback) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Generating Purchase Order for Requisition ID: " + requisitionId));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        int result = JOptionPane.showConfirmDialog(
                parent,
                panel,
                "General Purchase Order",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            PurchaseRequisition pr = requisitionRepo.find(requisitionId);
            if (pr != null) {
                showDetailedPRPanel(parent, pr,onGenerateCallback);
            } else {
                JOptionPane.showMessageDialog(parent, "Requisition not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDetailedPRPanel(Component parent, PurchaseRequisition pr,Runnable onGenerateCallback) {
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        User salesManager = userRepository.find(pr.getSalesManagerId());
        Item item = itemRepository .find(pr.getItemId());

        String salesManagerName = (salesManager != null) ? salesManager.getUsername() : "Unknown";
        String itemName = (item != null) ? item.getName() : "Unknown";

        detailPanel.add(new JLabel("Requisition ID: " + pr.getRequisitionId()));
        detailPanel.add(new JLabel("Sales Manager: " + salesManagerName + " (" + pr.getSalesManagerId() + ")"));
        detailPanel.add(new JLabel("Item: " + itemName + " (" + pr.getItemId() + ")"));
        detailPanel.add(new JLabel("Quantity: " + pr.getQuantity()));
        detailPanel.add(new JLabel("Generated Date: " + pr.getGeneratedDate()));
        detailPanel.add(new JLabel("Required Date: " + pr.getRequiredDate()));
        detailPanel.add(new JLabel("Status: " + pr.getStatus()));
        detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        int result = JOptionPane.showOptionDialog(
                parent,
                detailPanel,
                "Confirm Generate Purchase Order",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new String[]{"Generate", "Cancel"},
                "Generate"
        );

        if (result == JOptionPane.OK_OPTION) {
            generatePO(pr);
            JOptionPane.showMessageDialog(parent, "Purchase Order generated successfully!");
            onGenerateCallback.run();
        }
    }

    private void generatePO(PurchaseRequisition pr) {
        String poId = idGenerator.generateNewId(PurchaseOrder.class);
        String purchaseManagerId = "PM01"; // Replace this with actual manager ID if dynamic
        double price = 10.0; // Replace with actual logic to get price
        String today = LocalDate.now().toString();

        PurchaseOrder order = new PurchaseOrder(
                poId,
                pr.getItemId(),
                pr.getRequisitionId(),
                purchaseManagerId,
                pr.getQuantity(),
                price,
                today,
                PurchaseOrder.Status.pending
        );

        orderRepo.save(order);
    }
}
