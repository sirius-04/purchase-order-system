/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import models.users.PurchaseManager;

/**
 *
 * @author Chan Yong Liang
 */
public class PurchaseManagerController extends BaseController {
    public PurchaseManagerController(PurchaseManager user) {
        super(user);
    }

    @Override
    public void displayMenu() {
        System.out.println("Purchase manager");
    }
}
