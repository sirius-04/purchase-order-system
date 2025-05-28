package services;

import models.Item;
import models.PurchaseRequisition;
import models.users.SalesManager;
import models.users.User;
import repository.ItemRepository;
import repository.PurchaseRequisitionRepository;
import repository.UserRepository;

import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import utils.IdGenerator;

public class GeneratePR {

    private final UserRepository userRepository = new UserRepository();
    private final ItemRepository itemRepository = new ItemRepository();
    private final PurchaseRequisitionRepository requisitionRepository = new PurchaseRequisitionRepository();
    private final IdGenerator idGenerator = new IdGenerator();

    public void showForm(JFrame parentFrame, Runnable onSave) {
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
//            SalesManager selectedManager = (SalesManager) salesManagerDropdown.getSelectedItem();
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
}
