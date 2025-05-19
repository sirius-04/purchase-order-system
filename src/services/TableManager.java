/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import dtos.TableConvertible;

/**
 *
 * @author Chan Yong Liang
 */
public class TableManager {

    public static <T extends TableConvertible> void populateTable(DefaultTableModel model, List<T> dataObjects, boolean refresh) {
        if (refresh) {
            model.setRowCount(0);
        }

        for (T dataObject : dataObjects) {
            model.addRow(dataObject.toTableRow());
        }
    }
}
