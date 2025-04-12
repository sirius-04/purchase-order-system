/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import models.users.InventoryManager;

/**
 *
 * @author Chan Yong Liang
 */
public class InventoryManagerController extends BaseController {
    public InventoryManagerController(InventoryManager user) {
        super(user);
    }

    @Override
    public void displayMenu() {
        System.out.println("inventory manager");
    }
}
