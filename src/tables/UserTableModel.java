/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tables;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import models.users.User;
import repository.UserRepository;

/**
 *
 * @author ngoh
 */
public class UserTableModel extends AbstractTableModel {
    
    private final String[] columns = {
        "User ID",
        "Username",
        "Password",
        "Role"
    };
    
    private final UserRepository userRepo = new UserRepository();
    private List<User> users = new ArrayList<>();
    
    public UserTableModel() {
        refresh();
    }
    
    public void refresh() {
        users = userRepo.getAll();
        fireTableDataChanged();
    }
   
    @Override
    public int getRowCount() {
        return users.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }
    
    @Override
    public String getColumnName(int column) {
        return (column >= 0 && column < columns.length) ? columns[column] : super.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
         if (rowIndex < 0 || rowIndex >= users.size()) {
            return null;
        }

        User user = users.get(rowIndex);

        return switch (columnIndex) {
            case 0 ->
                user.getUserId();
            case 1 ->
                user.getUsername();
            case 2 ->
                user.getPassword();
            case 3 ->
                user.getUserRole();
            default ->
                null;
        };
    }
    
    public User getUserAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= users.size()) {
            return null;
        }
        return users.get(rowIndex);
    }
}
