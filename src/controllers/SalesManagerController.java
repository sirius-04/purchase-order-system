/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;
import models.Item;
import models.users.SalesManager;
import repository.ItemRepository;
import services.IdGenerator;
//import views.SalesManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class SalesManagerController extends BaseController {
    ItemRepository itemRes = new ItemRepository();
    IdGenerator idGen = new IdGenerator();
//    Item item1 = new Item(idGen.generateNewId(Item.class), "item5", 80, 44.30, "s002");
    
    public SalesManagerController(SalesManager sm) {
        super(sm);
    }
    
    @Override
    public void displayMenu() {
//        SalesManagerDashboard dashboard = new SalesManagerDashboard();
//        dashboard.setVisible(true);
        
        
//        itemRes.save(item1);
    }
}
