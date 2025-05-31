/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import models.Item;
import models.Sales;
import repository.ItemRepository;
import repository.SalesRepository;
import utils.DateTimeService;
import utils.IdGenerator;

/**
 *
 * @author Chan Yong Liang
 */
public class SalesService {

    private final IdGenerator idGenerator = new IdGenerator();
    private final SalesRepository salesRepo = new SalesRepository();
    private final ItemRepository itemRepo = new ItemRepository();

    public void addSale(Component parent) {

        List<Item> itemOnSaleList = itemRepo.getAll().stream()
                .filter(item -> item.getStatus() == Item.Status.onSale)
                .toList();

        JComboBox<Item> comboBox = new JComboBox<>(itemOnSaleList.toArray(new Item[0]));
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Select Item:"));
        panel.add(comboBox);
        panel.add(new JLabel("Select Quantity:"));
        panel.add(quantitySpinner);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Add Sale", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Item selectedItem = (Item) comboBox.getSelectedItem();
            int quantity = (Integer) quantitySpinner.getValue();

            if (quantity > selectedItem.getStockQuantity()) {
                insufficientStockWarning(parent, selectedItem);
                return;
            }

            String generatedSaleId = idGenerator.generateNewId(Sales.class);
            double totalAmount = selectedItem.getSellPrice() * quantity;

            String currentDate = DateTimeService.getCurrentDate();
            String currentTime = DateTimeService.getCurrentTime();

            Sales newSale = new Sales(
                    generatedSaleId,
                    selectedItem.getItemId(),
                    quantity,
                    currentDate,
                    currentTime,
                    totalAmount,
                    Sales.Status.added
            );

            int updatedStockQuantity = selectedItem.getStockQuantity() - quantity;
            selectedItem.setStockQuantity(updatedStockQuantity);

            salesRepo.save(newSale);
            itemRepo.update(selectedItem);

            JOptionPane.showMessageDialog(parent, "Sale recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    public void editSale(Component parent, Sales sale) {
        List<Item> itemOnSaleList = itemRepo.getAll().stream()
                .filter(item -> item.getStatus() == Item.Status.onSale)
                .toList();

        Item originalItem = itemRepo.find(sale.getItemId());
        int originalQuantity = sale.getQuantity();

        JComboBox<Item> comboBox = new JComboBox<>(itemOnSaleList.toArray(new Item[0]));
        comboBox.setSelectedItem(originalItem);

        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(originalQuantity, 1, 1000, 1));
        JTextField dateField = new JTextField(sale.getDate());
        JTextField timeField = new JTextField(sale.getTime());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Select Item:"));
        panel.add(comboBox);
        panel.add(new JLabel("Quantity:"));
        panel.add(quantitySpinner);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Time (HH:MM):"));
        panel.add(timeField);

        int result = JOptionPane.showConfirmDialog(parent, panel, "Edit Sale", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            Item selectedItem = (Item) comboBox.getSelectedItem();
            int newQuantity = (Integer) quantitySpinner.getValue();
            String newDate = dateField.getText().trim();
            String newTime = timeField.getText().trim();

            boolean isItemChanged = !originalItem.getItemId().equals(selectedItem.getItemId());
            int quantityDiff = newQuantity - originalQuantity;

            if (isItemChanged || quantityDiff != 0) {
                int confirmRestore = JOptionPane.showConfirmDialog(
                        parent,
                        "Do you want to restore the previously deducted stock?",
                        "Restore Stock",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmRestore == JOptionPane.YES_OPTION) {
                    if (isItemChanged) {
                        originalItem.setStockQuantity(originalItem.getStockQuantity() + originalQuantity);
                        itemRepo.update(originalItem);

                        selectedItem.setStockQuantity(selectedItem.getStockQuantity() - newQuantity);
                        itemRepo.update(selectedItem);
                    } else {
                        originalItem.setStockQuantity(originalItem.getStockQuantity() - quantityDiff);
                        itemRepo.update(originalItem);
                    }
                } else {
                    if (isItemChanged) {
                        selectedItem.setStockQuantity(selectedItem.getStockQuantity() - newQuantity);
                        itemRepo.update(selectedItem);
                    }
                }
            }

            sale.setItemId(selectedItem.getItemId());
            sale.setQuantity(newQuantity);
            sale.setDate(newDate);
            sale.setTime(newTime);
            sale.setTotalAmount(selectedItem.getSellPrice() * newQuantity);

            salesRepo.update(sale);

            JOptionPane.showMessageDialog(parent, "Sale updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void deleteSale(Component parent, Sales sale) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel("Confirm delete sale:"));
        panel.add(new JLabel("Sales ID: " + sale.getSalesId()));
        panel.add(new JLabel("Item ID: " + sale.getItemId()));
        panel.add(new JLabel("Time: " + sale.getTime()));

        int result = JOptionPane.showConfirmDialog(parent, panel, "Delete Sale", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            int restoreStock = JOptionPane.showConfirmDialog(
                    parent,
                    "Do you want to restore the deducted stock quantity?",
                    "Restore Stock",
                    JOptionPane.YES_NO_OPTION
            );

            if (restoreStock == JOptionPane.YES_OPTION) {
                Item item = itemRepo.find(sale.getItemId());
                if (item != null) {
                    item.setStockQuantity(item.getStockQuantity() + sale.getQuantity());
                    itemRepo.update(item);
                }
            }

            sale.setStatus(Sales.Status.deleted);
            salesRepo.update(sale);
            
            JOptionPane.showMessageDialog(parent, "Sale deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void displaySaleDetails(Component parent, Sales sale) {
        Item item = itemRepo.find(sale.getItemId());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Sale ID: " + sale.getSalesId()));
        panel.add(new JLabel("Item ID: " + sale.getItemId()));
        panel.add(new JLabel("Item Name: " + (item != null ? item.getName() : "Unknown")));
        panel.add(new JLabel("Quantity: " + sale.getQuantity()));
        panel.add(new JLabel("Date: " + sale.getDate()));
        panel.add(new JLabel("Time: " + sale.getTime()));
        panel.add(new JLabel(String.format("Total Amount: RM %.2f", sale.getTotalAmount())));

        Object[] options = {"Edit", "Delete", "Close"};
        int result = JOptionPane.showOptionDialog(
                parent,
                panel,
                "Sale Details",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[2]
        );

        if (result == 0) {
            editSale(parent, sale);
        } else if (result == 1) {
            deleteSale(parent, sale);
        }
    }

    public double calculateDailySalesTotal(String date) {
        
        return salesRepo.getAll().stream()
                .filter(sale -> date.equals(sale.getDate()))
                .mapToDouble(Sales::getTotalAmount)
                .sum();
    }

    public void insufficientStockWarning(Component parent, Item item) {
        JOptionPane.showMessageDialog(
                parent,
                "Insufficient stock for item: " + item.getName() + ".\nAvailable: " + item.getStockQuantity(),
                "Stock Warning",
                JOptionPane.WARNING_MESSAGE
        );
    }
}
