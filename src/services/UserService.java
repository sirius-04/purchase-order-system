/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import controllers.MainController;
import java.awt.Component;
import javax.swing.JOptionPane;
import models.users.Admin;
import models.users.FinanceManager;
import models.users.InventoryManager;
import models.users.PurchaseManager;
import models.users.SalesManager;
import models.users.User;
import models.users.UserRole;
import repository.UserRepository;
import utils.IdGenerator;
import views.AdminDashboard;

/**
 *
 * @author ngoh
 */
public class UserService {
    private final UserRepository userRepo = new UserRepository();
    IdGenerator idGen = new IdGenerator();
    
    public boolean usernameExists(String username) {
        return userRepo.getAll().stream()
            .anyMatch(user -> user.getUsername().equalsIgnoreCase(username));
    }
    
    public void registerUser(Component parent, String username, String password, UserRole role) {
        String newUserId = idGen.generateNewId(User.class);
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Username and password is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (usernameExists(username)) {
            JOptionPane.showMessageDialog(parent, "Username already exists", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (password.length() < 6) {
            JOptionPane.showMessageDialog(parent, "Password must be at least 6 characters!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        User newUser;
        
        switch (role) {
            case Admin -> newUser = new Admin(newUserId, role, username, password, Admin.Status.active);
            case SalesManager -> newUser = new SalesManager(newUserId, role, username, password, SalesManager.Status.active);
            case FinanceManager -> newUser = new FinanceManager(newUserId, role, username, password, FinanceManager.Status.active);
            case InventoryManager -> newUser = new InventoryManager(newUserId, role, username, password, InventoryManager.Status.active);
            case PurchaseManager -> newUser = new PurchaseManager(newUserId, role, username, password, PurchaseManager.Status.active);
            default -> throw new IllegalArgumentException("Unsupported role: " + role);
        }
        
        userRepo.save(newUser);
        
        JOptionPane.showMessageDialog(parent, "User created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        if (parent instanceof AdminDashboard adminDashboard) {
            adminDashboard.clearUserFields();
        }
    }
    
    public boolean renameUserOrChangePassword(Component parent, User user) {
        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(2, 2, 40, 10));
        javax.swing.JLabel usernameLabel = new javax.swing.JLabel("New Username:");
        javax.swing.JTextField usernameField = new javax.swing.JTextField(user.getUsername());
        javax.swing.JLabel passwordLabel = new javax.swing.JLabel("New Password:");
        javax.swing.JTextField passwordField = new javax.swing.JTextField();

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(
            parent, 
            panel, 
            "Update Username or Password", 
            JOptionPane.OK_CANCEL_OPTION, 
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String newUsername = usernameField.getText().trim();
            String newPassword = passwordField.getText().trim();

            boolean updated = false;

            if (!newUsername.equals(user.getUsername())) {
                if (newUsername.isEmpty()) {
                    JOptionPane.showMessageDialog(parent, "Username cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if (usernameExists(newUsername)) {
                    JOptionPane.showMessageDialog(parent, "Username already exists!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                user.setUsername(newUsername);
                updated = true;
            }

            if (!newPassword.isEmpty()) {
                if (newPassword.equals(user.getPassword())) {
                    JOptionPane.showMessageDialog(parent, "New password cannot be the same as the old password!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                if (newPassword.length() < 6) {
                    JOptionPane.showMessageDialog(parent, "Password must be at least 6 characters!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }

                user.setPassword(newPassword);
                updated = true;
            }
            
            if (updated) {
                userRepo.update(user);
                JOptionPane.showMessageDialog(parent, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(parent, "No changes were made.", "Information", JOptionPane.INFORMATION_MESSAGE);
            }

            return updated;
        }
        return false;
    }
    
    public void deleteUser(Component parent, User user) {
            userRepo.delete(user);
        }

    public void userLogOut(Component parent, User user) {
        int confirm = JOptionPane.showConfirmDialog(
            parent,
            "Are you sure you want to log out?",
            "Logout Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm == JOptionPane.YES_OPTION) {

            user = null;

            if (parent instanceof javax.swing.JFrame) {
                ((javax.swing.JFrame) parent).dispose();
            }

            new MainController().start();

            JOptionPane.showMessageDialog(
                null,
                "You have successfully logged out!",
                "Logout Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

}
