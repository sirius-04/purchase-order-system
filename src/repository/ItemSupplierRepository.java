/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.util.ArrayList;
import java.util.List;
import models.ItemSupplier;
import models.Supplier;

/**
 *
 * @author Chan Yong Liang
 */
public class ItemSupplierRepository extends BaseRepository<ItemSupplier> {

    private final SupplierRepository supplierRepo = new SupplierRepository();

    public ItemSupplierRepository() {
        super("item_supplier", "%s,%s");
    }

    public List<Supplier> getItemSupplier(String itemId) {
        List<ItemSupplier> itemSupplierList = getAll();
        List<Supplier> supplierList = new ArrayList<>();

        for (ItemSupplier itemSupplier : itemSupplierList) {
            if (itemSupplier.getItemId().equals(itemId)) {
                Supplier supplier = supplierRepo.find(itemSupplier.getSupplierId());
                if (supplier != null) {
                    supplierList.add(supplier);
                } else {
                    System.out.println("Warning: Supplier ID " + itemSupplier.getSupplierId() + " not found.");
                }
            }
        }

        return supplierList;
    }

    @Override
    protected String formatToRow(ItemSupplier itemSupplier) {
        return String.format(rowFormat,
                itemSupplier.getItemId(),
                itemSupplier.getSupplierId()
        );
    }

    @Override
    protected ItemSupplier parseRow(String[] columns) {
        String itemId = columns[0].trim();
        String supplierId = columns[1].trim();

        return new ItemSupplier(itemId, supplierId);
    }

}
