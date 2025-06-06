/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.util.List;
import models.Supplier;

/**
 *
 * @author dede
 */
public class SupplierRepository extends BaseRepository<Supplier> {

    public SupplierRepository() {
        super("suppliers", "%s,%s,%s,%s,%s,%s");
    }

    public boolean checkNameExists(String supplierName) {
        List<Supplier> supplierList = getAll();

        for (Supplier supplier : supplierList) {
            if (supplier.getName().equalsIgnoreCase(supplierName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected String formatToRow(Supplier supplier) {
        return String.format(rowFormat,
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getContactNum(),
                supplier.getEmail(),
                supplier.getAccountNum(),
                supplier.getStatus()
        );
    }

    @Override
    protected Supplier parseRow(String[] columns) {
        String supplierId = columns[0].trim();
        String name = columns[1].trim();
        String contactNum = columns[2].trim();
        String email = columns[3].trim();
        String accountNum = columns[4].trim();
        Supplier.Status status = Supplier.Status.valueOf(columns[5].trim());

        return new Supplier(supplierId, name, contactNum, email, accountNum, status);
    }

}
