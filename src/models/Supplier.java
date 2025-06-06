/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

/**
 *
 * @author Chan Yong Liang
 */
public class Supplier implements Identifiable {

    public enum Status {
        active,
        deleted
    }
    
    private String supplierId;
    private String name;
    private String contactNum;
    private String email;
    private String accountNum;
    private Status status;

    // Constructor
    public Supplier(String supplierId, String name, String contactNum, String email, String accountNum, Status status) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactNum = contactNum;
        this.email = email;
        this.accountNum = accountNum;
        this.status = status;
    }
    
    // override interface
    @Override
    public String getId() {
        return supplierId;
    }

    // Getters & Setters
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    
}
