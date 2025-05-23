/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 *
 * @author dede
 */
public class SupplierTableRow implements TableConvertible {
    private String supplierId;
    private String name;
    private String contactNum;
    private String email;

    public SupplierTableRow(String supplierId, String name, String contactNum, String email) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactNum = contactNum;
        this.email = email;
    }

    @Override
    public Object[] toTableRow() {
        return new Object[]{
          supplierId, name, contactNum, email
        };
    }
}
