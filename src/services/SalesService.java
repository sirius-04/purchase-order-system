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

    public boolean addSale(Component parent) {

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
                JOptionPane.showMessageDialog(
                        parent,
                        "Insufficient stock for the selected item.\nAvailable: " + selectedItem.getStockQuantity(),
                        "Stock Warning",
                        JOptionPane.WARNING_MESSAGE
                );
                return false;
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
                    totalAmount
            );

            int updatedStockQuantity = selectedItem.getStockQuantity() - quantity;
            selectedItem.setStockQuantity(updatedStockQuantity);

            salesRepo.save(newSale);
            itemRepo.update(selectedItem);

            JOptionPane.showMessageDialog(parent, "Sale recorded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }

        return false;
    }

    public double calculateTodaySalesTotal() {
        String today = DateTimeService.getCurrentDate();

        return salesRepo.getAll().stream()
                .filter(sale -> today.equals(sale.getDate()))
                .mapToDouble(Sales::getTotalAmount)
                .sum();
    }
}
