/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import dtos.DailySalesTableRow;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import models.Item;
import models.Sales;
import models.users.SalesManager;
import repository.ItemRepository;
import repository.SalesRepository;
import services.TableManager;
import views.SalesManagerDashboard;

/**
 *
 * @author Chan Yong Liang
 */
public class SalesManagerController extends BaseController {

    private SalesManagerDashboard dashboard;
    private JTable dailySalesTable;

    public SalesManagerController(SalesManager salesManagerUser) {
        super(salesManagerUser);
    }

    @Override
    protected JFrame createView() {
        dashboard = new SalesManagerDashboard();
        return dashboard;
    }

    @Override
    protected void loadInitialData() {
        dailySalesTable = dashboard.getSalesTable();
        DefaultTableModel model = (DefaultTableModel) dailySalesTable.getModel();

        SalesRepository salesRepo = new SalesRepository();
        ItemRepository itemRepo = new ItemRepository();

        String today = LocalDate.now().toString(); 

        List<Sales> todaysSales = salesRepo.getAll().stream()
                .filter(sale -> today.equals(sale.getDate()))
                .toList();

        List<DailySalesTableRow> rows = new ArrayList<>();
        for (Sales sale : todaysSales) {
            try {
                Item item = itemRepo.find(sale.getItemId());

                DailySalesTableRow row = new DailySalesTableRow(
                        sale.getTime(),
                        sale.getSalesId(),
                        sale.getItemId(),
                        item.getName(),
                        sale.getQuantity(),
                        item.getPrice(),
                        sale.getTotalAmount()
                );
                rows.add(row);
            } catch (Exception e) {
                System.err.println("Item not found for ID: " + sale.getItemId());
            }
        }

        TableManager.populateTable(model, rows, true);
    }

    @Override
    protected void setupCustomListeners() {

    }
}
