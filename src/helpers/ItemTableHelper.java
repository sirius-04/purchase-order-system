/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpers;

/**
 *
 * @author Chan Yong Liang
 */
import dtos.ItemTableRow;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import models.Item;
import models.Supplier;
import repository.ItemRepository;
import repository.SupplierRepository;
import utils.TableManager;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemTableHelper extends BaseTableHelper {

    public static void populateItemOnSale(DefaultTableModel model) {
        ItemRepository itemRepo = new ItemRepository();
        SupplierRepository supplierRepo = new SupplierRepository();

        List<Item> itemList = itemRepo
                .getAll()
                .stream()
                .filter(item -> item.getStatus() == Item.Status.onSale)
                .toList();

        List<ItemTableRow> rows = new ArrayList<>();

        for (Item item : itemList) {
            try {
                Supplier supplier = supplierRepo.find(item.getSupplierId());

                ItemTableRow row = new ItemTableRow(item.getId(), item.getName(), item.getPrice(), item.getStockQuantity(), supplier.getName());

                rows.add(row);

            } catch (Exception e) {
                System.err.println("Supplier not found: " + item.getSupplierId());
            }

        }

        if (rows.isEmpty()) {
            model.setRowCount(0);
            model.addRow(new Object[]{"No item on sale", "", "", "", ""});
        } else {
            TableManager.populateTable(model, rows, true);
        }

    }

    public static void populateItemNotOnSale(DefaultTableModel model) {
        ItemRepository itemRepo = new ItemRepository();
        SupplierRepository supplierRepo = new SupplierRepository();

        List<Item> itemList = itemRepo
                .getAll()
                .stream()
                .filter(item -> item.getStatus() == Item.Status.notOnSale)
                .toList();

        List<ItemTableRow> rows = new ArrayList<>();

        for (Item item : itemList) {
            try {
                Supplier supplier = supplierRepo.find(item.getSupplierId());

                ItemTableRow row = new ItemTableRow(item.getId(), item.getName(), item.getPrice(), item.getStockQuantity(), supplier.getName());

                rows.add(row);

            } catch (Exception e) {
                System.err.println("Supplier not found: " + item.getSupplierId());
            }

        }

        if (rows.isEmpty()) {
            model.setRowCount(0);
            model.addRow(new Object[]{"No item", "", "", "", ""});
        } else {
            TableManager.populateTable(model, rows, true);
        }

    }
}
