/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CustomPanel;

import java.awt.Color;
import javax.swing.JTabbedPane;

/**
 *
 * @author ngoh
 */
public class TabbedPaneCustom extends JTabbedPane {

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
        repaint();
    }

    public Color getUnselectedColor() {
        return unselectedColor;
    }

    public void setUnselectedColor(Color unselectedColor) {
        this.unselectedColor = unselectedColor;
        repaint();
    }

    private Color selectedColor = new Color(248, 91, 50);
    private Color unselectedColor = new Color(230, 230, 230);

    public TabbedPaneCustom() {
        setBackground(new Color(250, 250, 250));
        setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        setUI(new TabbedPaneCustomUI(this));
    }
}
