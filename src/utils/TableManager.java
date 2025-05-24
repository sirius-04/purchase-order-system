/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import dtos.TableConvertible;
import java.util.ArrayList;

/**
 *
 * @author Chan Yong Liang
 */
public class TableManager<T extends TableConvertible> {
    private List<T> backingList = new ArrayList<>();

    public void populateTable(DefaultTableModel model, List<T> dataObjects, boolean refresh) {
        if (refresh) {
            model.setRowCount(0);
            backingList.clear();
        }

        for (T dataObject : dataObjects) {
            model.addRow(dataObject.toTableRow());
            backingList.add(dataObject);
        }
    }

    public T getObjectAt(int rowIndex) {
        return backingList.get(rowIndex);
    }

    public List<T> getAll() {
        return backingList;
    }
}

