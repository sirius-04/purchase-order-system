package controllers;
import models.User;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryManager extends User {
    public InventoryManager(String userId, String username, String password) {
        super(userId, username, password);
    }
    
    @Override
    public void displayMenu() {
        System.out.println("inventoryyyyyy");
    }
    
    // role based function here
}
