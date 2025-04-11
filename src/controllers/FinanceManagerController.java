/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import models.users.FinanceManager;

/**
 *
 * @author Chan Yong Liang
 */
public class FinanceManagerController extends BaseController {
    public FinanceManagerController(FinanceManager user) {
        super(user);
    }
    
    
    @Override
    public void displayMenu() {
        System.out.println("finance manager");
    }
    
}
