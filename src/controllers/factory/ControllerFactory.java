/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers.factory;

import controllers.BaseController;
import java.util.Map;
import models.users.Admin;
import models.users.FinanceManager;
import models.users.InventoryManager;
import models.users.PurchaseManager;
import models.users.SalesManager;
import models.users.User;
import models.users.UserRole;

/**
 *
 * @author Chan Yong Liang
 */
// Inside ControllerFactory.java

public class ControllerFactory {
    private static final Map<Class<? extends User>, UserRole> roleMap = Map.of(
        Admin.class, UserRole.ADMIN,
        SalesManager.class, UserRole.SALES_MANAGER,
        FinanceManager.class, UserRole.FINANCE_MANAGER,
        InventoryManager.class, UserRole.INVENTORY_MANAGER,
        PurchaseManager.class, UserRole.PURCHASE_MANAGER
    );

    public static BaseController getControllerFor(User user) {
        UserRole role = roleMap.get(user.getClass());

        if (role == null) {
            throw new IllegalArgumentException("Unknown user role: " + user.getClass().getName());
        }

        try {
            return role.getControllerClass()
                       .getConstructor(user.getClass())
                       .newInstance(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate controller for " + role, e);
        }
    }
}




