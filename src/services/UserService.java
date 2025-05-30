/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

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
}
