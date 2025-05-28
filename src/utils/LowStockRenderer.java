/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

/**
 *
 * @author dede
 */
import java.awt.Component;
import java.awt.Color;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class LowStockRenderer extends DefaultTableCellRenderer {
    private final int quantityColumnIndex;

    // Constructor - insert the table's quantity index (0, 1, 2, 3...)
    public LowStockRenderer(int quantityColumnIndex) {
        this.quantityColumnIndex = quantityColumnIndex;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        // Color rules 
        Color lowColor = Color.PINK;
        Color standardColor = Color.WHITE;
        
        // Quantity rules
        int lowStock =  20;

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        Object quantityObj = table.getModel().getValueAt(row, quantityColumnIndex);

        Color backgroundColor = isSelected ? table.getSelectionBackground() : standardColor;

        if (quantityObj instanceof Integer) {
            int quantity = (Integer) quantityObj;
            if (quantity < lowStock) {
                backgroundColor = lowColor;
            } else {
                backgroundColor = standardColor;
            }

            // Keep selection color override
            if (isSelected) {
                backgroundColor = table.getSelectionBackground();
            }
        }

        c.setBackground(backgroundColor);
        return c;
    }
}
