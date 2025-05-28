/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.awt.Component;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import models.Supplier;
import repository.SupplierRepository;
import utils.IdGenerator;

/**
 *
 * @author Chan Yong Liang
 */
public class SupplierService {
    
    private IdGenerator idGenerator =  new IdGenerator();
    private SupplierRepository supplierRepo = new SupplierRepository();

    public Supplier addSupplier(Component parent) {
        JTextField sNameField = new JTextField();
        JTextField sContactField = new JTextField();
        JTextField sEmailField = new JTextField();
        JTextField sBankField = new JTextField();

        JPanel newSupplierPanel = new JPanel();
        newSupplierPanel.setLayout(new BoxLayout(newSupplierPanel, BoxLayout.Y_AXIS));
        newSupplierPanel.add(new JLabel("Supplier Name:"));
        newSupplierPanel.add(sNameField);
        newSupplierPanel.add(new JLabel("Contact Number:"));
        newSupplierPanel.add(sContactField);
        newSupplierPanel.add(new JLabel("Email:"));
        newSupplierPanel.add(sEmailField);
        newSupplierPanel.add(new JLabel("Bank Account Number:"));
        newSupplierPanel.add(sBankField);

        int newSupplierResult = JOptionPane.showConfirmDialog(
                parent,
                newSupplierPanel,
                "Create New Supplier",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (newSupplierResult != JOptionPane.OK_OPTION) {
            return null;
        }

        String supplierName = sNameField.getText().trim();
        String contact = sContactField.getText().trim();
        String email = sEmailField.getText().trim();
        String bankAccount = sBankField.getText().trim();

        if (supplierName.isEmpty() || contact.isEmpty() || email.isEmpty() || bankAccount.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "All supplier fields must be filled.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        String newSupplierId = idGenerator.generateNewId(Supplier.class);
        Supplier newSupplier = new Supplier(newSupplierId, supplierName, contact, email, bankAccount);
        supplierRepo.save(newSupplier);
        return newSupplier;
    }
}
