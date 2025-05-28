/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import models.Supplier;

/**
 *
 * @author dede
 */
public class SupplierRepository extends BaseRepository<Supplier>{
    
     public SupplierRepository() {
        super("suppliers", "%s,%s,%s,%s,%s");
    }
    
     @Override
    protected String formatToRow(Supplier supplier) {
        return String.format(rowFormat,
                supplier.getSupplierId(),
                supplier.getName(),
                supplier.getContactNum(),
                supplier.getEmail(),
                supplier.getAccountNum()
        );
    }

    @Override
    protected Supplier parseRow(String[] columns) {
        String supplierId = columns[0].trim();
        String name = columns[1].trim();
        String contactNum = columns[2].trim();
        String email = columns[3].trim();
        String accountNum = columns[4].trim();

        return new Supplier(supplierId, name, contactNum, email, accountNum);
    }
    
}
