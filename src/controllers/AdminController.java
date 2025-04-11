/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import models.users.Admin;

/**
 *
 * @author Chan Yong Liang
 */
public class AdminController extends BaseController {
    public AdminController(Admin adminUser) {
        super(adminUser);
    }

    @Override
    public void displayMenu() {
        System.out.println("admin");
    }
}
