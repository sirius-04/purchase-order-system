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
    Admin(AdminController.class, Admin.class),
    SalesManager(SalesManagerController.class, SalesManager.class),
    FinanceManager(FinanceManagerController.class, FinanceManager.class),
    InventoryManager(InventoryManagerController.class, InventoryManager.class),
    PurchaseManager(PurchaseManagerController.class, PurchaseManager.class);

    private final Class<? extends BaseController> controllerClass;
    private final Class<? extends User> modelClass;

    UserRole(Class<? extends BaseController> controllerClass, Class<? extends User> modelClass) {
        this.controllerClass = controllerClass;
        this.modelClass = modelClass;
    }

    public Class<? extends BaseController> getControllerClass() {
        return controllerClass;
    }

    public Class<? extends User> getModelClass() {
        return modelClass;
    }
}
