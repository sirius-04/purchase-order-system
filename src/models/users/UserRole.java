/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models.users;

import controllers.AdminController;
import controllers.BaseController;
import controllers.FinanceManagerController;
import controllers.InventoryManagerController;
import controllers.PurchaseManagerController;
import controllers.SalesManagerController;

/**
 *
 * @author Chan Yong Liang
 */
public enum UserRole {
    ADMIN(AdminController.class),
    SALES_MANAGER(SalesManagerController.class),
    FINANCE_MANAGER(FinanceManagerController.class),
    INVENTORY_MANAGER(InventoryManagerController.class),
    PURCHASE_MANAGER(PurchaseManagerController.class);

    private Class<? extends BaseController> controllerClass;

    UserRole(Class<? extends BaseController> controllerClass) {
        this.controllerClass = controllerClass;
    }

    public Class<? extends BaseController> getControllerClass() {
        return controllerClass;
    }
}

