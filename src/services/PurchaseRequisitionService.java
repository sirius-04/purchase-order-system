/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

/**
 *
 * @author Acer
 */
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import models.Item;
import models.PurchaseRequisition;
import models.users.SalesManager;
import repository.ItemRepository;
import repository.PurchaseRequisitionRepository;
import repository.UserRepository;
import utils.IdGenerator;

import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import tables.PendingPurchaseRequisitionTableModel;

public class PurchaseRequisitionService {

    private final UserRepository userRepository = new UserRepository();
    private final ItemRepository itemRepository = new ItemRepository();
    private final PurchaseRequisitionRepository requisitionRepository = new PurchaseRequisitionRepository();
    private final IdGenerator idGenerator = new IdGenerator();

    public void showCreateForm(JFrame parentFrame, Runnable onSave) {
        List<SalesManager> salesManagers = userRepository.getAll()
            .stream()
            .filter(user -> user instanceof SalesManager)
            .map(user -> (SalesManager) user)
            .toList();

        List<String> salesManagerNames = salesManagers.stream()
            .map(SalesManager::getUsername)
            .toList();

        List<Item> items = itemRepository.getAll();

        JComboBox<String> salesManagerDropdown = new JComboBox<>(salesManagerNames.toArray(new String[0]));
        JComboBox<Item> itemDropdown = new JComboBox<>(items.toArray(new Item[0]));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));
        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel());
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Select Sales Manager:"));
        panel.add(salesManagerDropdown);
        panel.add(new JLabel("Select Item:"));
        panel.add(itemDropdown);
        panel.add(new JLabel("Enter Quantity:"));
        panel.add(quantitySpinner);
        panel.add(new JLabel("Select Required Date:"));
        panel.add(dateSpinner);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Add Purchase Requisition", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String selectedManagerName = (String) salesManagerDropdown.getSelectedItem();
            SalesManager selectedManager = salesManagers.stream()
                .filter(manager -> manager.getUsername().equals(selectedManagerName))
                .findFirst()
                .orElse(null);

            Item selectedItem = (Item) itemDropdown.getSelectedItem();
            int quantity = (Integer) quantitySpinner.getValue();
            Date requiredDate = (Date) dateSpinner.getValue();

            LocalDate generatedDate = LocalDate.now();
            LocalDate requiredLocalDate = requiredDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            String newRequisitionId = idGenerator.generateNewId(PurchaseRequisition.class);

            requisitionRepository.save(new PurchaseRequisition(
                newRequisitionId,
                selectedManager.getId(),
                selectedItem.getId(),
                quantity,
                generatedDate.toString(),
                requiredLocalDate.toString(),
                PurchaseRequisition.Status.pending
            ));

            if (onSave != null) {
                onSave.run();
            }

            JOptionPane.showMessageDialog(parentFrame, "Purchase requisition saved!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    public void showActionPanel(PurchaseRequisition requisition, JFrame parentFrame, Runnable onChange) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Choose an action for Requisition ID: " + requisition.getRequisitionId()));

        int result = JOptionPane.showOptionDialog(parentFrame, panel, "Requisition Actions",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                new String[]{"Edit", "Delete", "Cancel"}, "Edit");

        if (result == 0) { // Edit
            showEditForm(requisition, parentFrame, onChange);
        } else if (result == 1) { // Delete
            requisition.setStatus(PurchaseRequisition.Status.deleted);
            requisitionRepository.update(requisition);
            JOptionPane.showMessageDialog(parentFrame, "Requisition marked as deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
            if (onChange != null) onChange.run();
        }
}

    public void showEditForm(PurchaseRequisition requisition, JFrame parentFrame, Runnable onSave) {
        List<Item> items = itemRepository.getAll();

        JComboBox<Item> itemDropdown = new JComboBox<>(items.toArray(new Item[0]));
        itemDropdown.setSelectedItem(itemRepository.find(requisition.getItemId()));

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(requisition.getQuantity(), 1, 1000, 1));

        LocalDate requiredDate = LocalDate.parse(requisition.getRequiredDate());
        Date dateValue = Date.from(requiredDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        JSpinner dateSpinner = new JSpinner(new SpinnerDateModel(dateValue, null, null, java.util.Calendar.DAY_OF_MONTH));
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Edit Item:"));
        panel.add(itemDropdown);
        panel.add(new JLabel("Edit Quantity:"));
        panel.add(quantitySpinner);
        panel.add(new JLabel("Edit Required Date:"));
        panel.add(dateSpinner);

        int result = JOptionPane.showConfirmDialog(parentFrame, panel, "Edit Purchase Requisition", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Item selectedItem = (Item) itemDropdown.getSelectedItem();
            int quantity = (Integer) quantitySpinner.getValue();
            Date requiredDateObj = (Date) dateSpinner.getValue();
            LocalDate requiredLocalDate = requiredDateObj.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            requisition.setItemId(selectedItem.getId());
            requisition.setQuantity(quantity);
            requisition.setRequiredDate(requiredLocalDate.toString());

            requisitionRepository.update(requisition);
            JOptionPane.showMessageDialog(parentFrame, "Requisition updated successfully!", "Updated", JOptionPane.INFORMATION_MESSAGE);

            if (onSave != null) {
                onSave.run();
            }
        }
    }
}
