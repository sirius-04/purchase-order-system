/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpers;

/**
 *
 * @author Chan Yong Liang
 */
import dtos.DailySalesTableRow;
import models.Sales;
import models.Item;
import repository.ItemRepository;
import repository.SalesRepository;
import utils.TableManager;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import utils.DateTimeService;

public class DailySalesTableHelper extends BaseTableHelper {

    public static void populateTodaySales(JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        
        SalesRepository salesRepo = new SalesRepository();
        ItemRepository itemRepo = new ItemRepository();

        String today = DateTimeService.getCurrentDate();

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
                        item.getSellPrice(),
                        sale.getTotalAmount()
                );
                rows.add(row);
            } catch (Exception e) {
                System.err.println("Item not found: " + sale.getItemId());
            }
        }

        if (rows.isEmpty()) {
            model.setRowCount(0);
            model.addRow(new Object[]{"No sales yet", "", "", "", "", "", ""});
        } else {
            TableManager.populateTable(model, rows, true);
            
        }
    }
}
