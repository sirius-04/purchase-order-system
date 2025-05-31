/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import models.PurchaseOrder;
import models.Item;
import models.Supplier;
import models.InventoryUpdate;
import models.PurchaseRequisition;
import models.users.User;
import repository.ItemRepository;
import repository.ItemSupplierRepository;
import repository.PurchaseOrdersRepository;
import repository.SupplierRepository;
import repository.InventoryUpdateRepository;
import repository.PurchaseRequisitionRepository;
import repository.UserRepository;
import tables.PurchaseOrderTableModel;
import utils.IdGenerator;

/**
 *
 * @author ngoh
 */
public class PurchaseOrderService {

    private final PurchaseOrdersRepository purchaseOrderRepo = new PurchaseOrdersRepository();
    private final SupplierRepository supplierRepo = new SupplierRepository();
    private final ItemRepository itemRepo = new ItemRepository();
    private final ItemSupplierRepository itemSupplierRepo = new ItemSupplierRepository();
    private final InventoryUpdateRepository inventoryUpdateRepo = new InventoryUpdateRepository();
    private final PurchaseRequisitionRepository requisitionRepo = new PurchaseRequisitionRepository();
    private final IdGenerator idGenerator = new IdGenerator();
    private final UserRepository userRepository = new UserRepository();
    private final ItemRepository itemRepository = new ItemRepository();


    private String[] getSuppliers(String itemId) {
        List<Supplier> supplierList = itemSupplierRepo.getItemSupplier(itemId);
        String[] supplierOptions = new String[supplierList.size()];

        for (int i = 0; i < supplierList.size(); i++) {
            supplierOptions[i] = supplierList.get(i).getSupplierId();
        }

        return supplierOptions;
    }

    public void addApprovalListener(Component parent, PurchaseOrder po) {
        
        int currentQuantity = po.getQuantity();
        Supplier currentSupplier = supplierRepo.find(po.getSupplierId());

        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(2, 2, 5, 5));
        panel.setPreferredSize(new java.awt.Dimension(350, 60));
        javax.swing.JTextField quantityField = new javax.swing.JTextField(String.valueOf(currentQuantity));

        String[] supplierOptions = getSuppliers(po.getItemId());
        javax.swing.JComboBox<String> supplierCombo = new javax.swing.JComboBox<>(supplierOptions);
        for (int i = 0; i < supplierOptions.length; i++) {
            if (supplierOptions[i].contains(currentSupplier.getName())) {
                supplierCombo.setSelectedIndex(i);
                break;
            }
        }

        panel.add(new javax.swing.JLabel("Quantity:"));
        panel.add(quantityField);
        panel.add(new javax.swing.JLabel("Supplier:"));
        panel.add(supplierCombo);

        int option = javax.swing.JOptionPane.showConfirmDialog(
                parent,
                panel,
                "Edit Purchase Order",
                javax.swing.JOptionPane.OK_CANCEL_OPTION,
                javax.swing.JOptionPane.PLAIN_MESSAGE
        );

        if (option == javax.swing.JOptionPane.OK_OPTION) {
            String newQuantity = quantityField.getText();
            String newSupplierId = supplierCombo.getSelectedItem().toString();

            if (newQuantity.isEmpty()) {
                javax.swing.JOptionPane.showMessageDialog(parent, "Quantity cannot be empty.", "Warning", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    parent,
                    "Do you want to approve this purchase order?",
                    "Approve Purchase Order",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                int updatedQuantity = Integer.parseInt(newQuantity);

                double newPrice = updatedQuantity * itemRepo.find(po.getItemId()).getPrice();

                PurchaseOrder updatedPO = new PurchaseOrder(po.getPurchaseOrderId(), po.getItemId(), 
                                                            po.getPurchaseRequisitionId(), po.getUserId(), 
                                                            Integer.parseInt(newQuantity), newPrice, po.getDate(), 
                                                            PurchaseOrder.Status.approved, newSupplierId);

                purchaseOrderRepo.update(updatedPO);

                javax.swing.JOptionPane.showMessageDialog(parent, "Approved successfully!", "Success", javax.swing.JOptionPane.INFORMATION_MESSAGE);

            } else {
                javax.swing.JOptionPane.showMessageDialog(parent, "Failed to approve. Please try again.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void verifyPO(Component parent, PurchaseOrder po) {
        if (po == null) return;
        
        int result = JOptionPane.showConfirmDialog(
            parent,
            "Are you sure you want to verify this Purchase Order?",
            "Confirm Verification",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (result == JOptionPane.YES_OPTION) {
            
            // 1. Change the PO status to 'verify'
            po.setStatus(PurchaseOrder.Status.verified);
            purchaseOrderRepo.update(po); 
            
            // 2. Edit the stock quantity in item.text
            int verifiedItemQuantity = po.getQuantity();
            int newStockQuantity = verifiedItemQuantity + itemRepo.find(po.getItemId()).getStockQuantity();
            
           
            Item updatedItem = new Item(po.getItemId(), itemRepo.find(po.getItemId()).getName(), 
                                        newStockQuantity, itemRepo.find(po.getItemId()).getPrice(), itemRepo.find(po.getItemId()).getSellPrice(), 
                                        itemRepo.find(po.getItemId()).getSupplierId(), itemRepo.find(po.getItemId()).getStatus()
                                        );

            itemRepo.update(updatedItem);
            
            // 3. generate new inventory update
            IdGenerator idGen = new IdGenerator();
            String newInventoryUpdateId = idGen.generateNewId(InventoryUpdate.class);
            
            InventoryUpdate newInventoryUpdate = new InventoryUpdate(
                    newInventoryUpdateId,
                    po.getItemId(),
                    po.getSupplierId(),
                    po.getQuantity(),
                    po.getPrice(),
                    models.InventoryUpdate.Status.pending
            );

            inventoryUpdateRepo.save(newInventoryUpdate);


            JOptionPane.showMessageDialog(parent, "Purchase Order had verified. New stock quantity is updated!");
        }
    }
    
     public void setupGeneratePOListener(JTable requisitionTable, User userEditor, Runnable onGenerateCallback) {
    requisitionTable.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int row = requisitionTable.getSelectedRow();
                if (row >= 0) {
                    String requisitionId = requisitionTable.getValueAt(row, 0).toString();
                    PurchaseRequisition pr = requisitionRepo.find(requisitionId);

                    if (pr != null) {
                        showPODialog(requisitionTable, requisitionId, userEditor, onGenerateCallback);
                    }
                }
            }
        }
    });
}
    
    public void showPODialog(Component parent, String requisitionId, User userEditor,Runnable onGenerateCallback) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Generating Purchase Order for Requisition ID: " + requisitionId));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        int result = JOptionPane.showConfirmDialog(
            parent, panel, "Generate Purchase Order",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            PurchaseRequisition pr = requisitionRepo.find(requisitionId);
            if (pr != null) {
                showDetailedPRPanel(parent, pr, userEditor, onGenerateCallback);
            } else {
                JOptionPane.showMessageDialog(parent, "Requisition not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


     private void showDetailedPRPanel(Component parent, PurchaseRequisition pr, User userEditor, Runnable onGenerateCallback) {
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        User salesManager = userRepository.find(pr.getUserId());
        Item item = itemRepository.find(pr.getItemId());

        String salesManagerName = (salesManager != null) ? salesManager.getUsername() : "Unknown";
        String itemName = (item != null) ? item.getName() : "Unknown";

        detailPanel.add(new JLabel("Requisition ID: " + pr.getRequisitionId()));
        detailPanel.add(new JLabel("Sales Manager: " + salesManagerName + " (" + pr.getUserId()+ ")"));
        detailPanel.add(new JLabel("Item: " + itemName + " (" + pr.getItemId() + ")"));
        detailPanel.add(new JLabel("Quantity: " + pr.getQuantity()));
        detailPanel.add(new JLabel("Generated Date: " + pr.getGeneratedDate()));
        detailPanel.add(new JLabel("Required Date: " + pr.getRequiredDate()));
        detailPanel.add(new JLabel("Status: " + pr.getStatus()));
        detailPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        String[] options = {"Generate", "Reject", "Cancel"};
        int result = JOptionPane.showOptionDialog(
            parent,
            detailPanel,
            "Confirm Purchase Order Action",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );

        if (result == JOptionPane.YES_OPTION) {
            generatePO(pr, userEditor); 
            pr.setStatus(PurchaseRequisition.Status.approved);
            requisitionRepo.update(pr);
            JOptionPane.showMessageDialog(parent, "Purchase Order generated and requisition approved.");
            onGenerateCallback.run();
        } else if (result == JOptionPane.NO_OPTION) { 
            pr.setStatus(PurchaseRequisition.Status.rejected);
            requisitionRepo.update(pr);
            JOptionPane.showMessageDialog(parent, "Requisition has been rejected.");
            onGenerateCallback.run();
        }
    }

    public void generatePO(PurchaseRequisition pr, User userEditor) {
        String poId = idGenerator.generateNewId(PurchaseOrder.class);
        String userId = userEditor.getUserId();       
        String itemId = pr.getItemId();
        Item item = itemRepository.find(itemId);
        double price = item.getPrice() * pr.getQuantity(); 
        String today = LocalDate.now().toString();
        List<Supplier> suppliers = itemSupplierRepo.getItemSupplier(pr.getItemId());
        String supplierId = suppliers.isEmpty() ? "" : suppliers.get(0).getSupplierId();

        PurchaseOrder order = new PurchaseOrder(
            poId,
            pr.getItemId(),
            pr.getRequisitionId(),
            userId,
            pr.getQuantity(),
            price,
            today,
            PurchaseOrder.Status.pending,
            supplierId
        );

        purchaseOrderRepo.save(order);
        pr.setStatus(PurchaseRequisition.Status.approved);
        requisitionRepo.update(pr); 
    }
    
    
   public void setupPOTableClickListener(JTable poTable, User userEditor, Runnable onUpdate) {
        poTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = poTable.getSelectedRow();
                    if (row == -1) return;

                    PurchaseOrderTableModel model = (PurchaseOrderTableModel) poTable.getModel();
                    PurchaseOrder order = model.getPurchaseOrderAt(row);

                    showPOActionPanel(poTable, order, userEditor, onUpdate);
                }
            }
        });
    }
    private void showPOActionPanel(Component parent, PurchaseOrder po, User userEditor, Runnable onUpdateCallback) {
        SwingUtilities.invokeLater(() -> {
            JPanel buttonPanel = new JPanel();
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); 

            JButton editButton = new JButton("Edit");
            JButton deleteButton = new JButton("Delete");
            JButton cancelButton = new JButton("Cancel");

            buttonPanel.add(editButton);
            buttonPanel.add(deleteButton);
            buttonPanel.add(cancelButton);

            JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
            contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
            contentPanel.add(new JLabel("Choose an action for Purchase Order ID: " + po.getPurchaseOrderId()), BorderLayout.NORTH);
            contentPanel.add(buttonPanel, BorderLayout.SOUTH);

            JDialog dialog = new JDialog((Frame) null, "PO Actions", true);
            dialog.setContentPane(contentPanel);
            dialog.setPreferredSize(new Dimension(400, 200)); 
            dialog.pack();
            dialog.setLocationRelativeTo(parent);

            editButton.addActionListener(e -> {
                dialog.dispose();
                showEditPODialog(po, userEditor, onUpdateCallback);
            });

            deleteButton.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(parent, "Mark this Purchase Order as deleted?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        po.setStatus(PurchaseOrder.Status.deleted); 
                        purchaseOrderRepo.update(po); 
                        JOptionPane.showMessageDialog(parent, "Marked as deleted successfully!");
                        dialog.dispose();
                        if (onUpdateCallback != null) onUpdateCallback.run();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(parent, "Failed to update status: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

            cancelButton.addActionListener(e -> dialog.dispose());

            dialog.setVisible(true);
        });
    }
    
    private void showEditPODialog(PurchaseOrder po, User userEditor, Runnable onUpdateCallback) {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10)); 
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); 

        JComboBox<Item> itemDropdown = new JComboBox<>();
        for (Item item : itemRepository.getAll()) {
            itemDropdown.addItem(item);
        }

        Item currentItem = itemRepository.find(po.getItemId());
        if (currentItem != null) itemDropdown.setSelectedItem(currentItem);

        JTextField quantityField = new JTextField(String.valueOf(po.getQuantity()));

        panel.add(new JLabel("Item:"));
        panel.add(itemDropdown);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantityField);

        JButton updateBtn = new JButton("Update");
        JButton cancelBtn = new JButton("Cancel");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(updateBtn);
        buttonPanel.add(cancelBtn);

        JDialog dialog = new JDialog((Frame) null, "Edit Purchase Order", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setPreferredSize(new Dimension(450, 250)); 
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        updateBtn.addActionListener(ev -> {
            try {
                Item selectedItem = (Item) itemDropdown.getSelectedItem();
                int qty = Integer.parseInt(quantityField.getText().trim());

                if (selectedItem == null) {
                    JOptionPane.showMessageDialog(null, "Item not found!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                PurchaseOrder updatedPO = new PurchaseOrder(
                    po.getPurchaseOrderId(),
                    selectedItem.getItemId(),
                    po.getPurchaseRequisitionId(),
                    userEditor.getUserId(),
                    qty,
                    selectedItem.getPrice() * qty,
                    LocalDate.now().toString(),
                    po.getStatus(), 
                    selectedItem.getSupplierId()
                );

                purchaseOrderRepo.update(updatedPO);
                JOptionPane.showMessageDialog(null, "Updated successfully!");
                dialog.dispose();
                if (onUpdateCallback != null) onUpdateCallback.run();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid quantity", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Update failed: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelBtn.addActionListener(ev -> dialog.dispose());

        dialog.setVisible(true);
    }
}

