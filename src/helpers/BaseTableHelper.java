/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpers;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author Chan Yong Liang
 */
public abstract class BaseTableHelper {

    public static String calculateColumnTotal(JTable table, int columnIndex) {
        double total = 0;
        TableModel model = table.getModel();

        for (int row = 0; row < model.getRowCount(); row++) {
            Object value = model.getValueAt(row, columnIndex);

            if (value instanceof Number) {
                total += ((Number) value).doubleValue();
            } else if (value instanceof String) {
                try {
                    total += Double.parseDouble((String) value);
                } catch (NumberFormatException e) {
                    //
                }
            }
        }

        return "RM " + String.format("%.2f", total);
    }
}
