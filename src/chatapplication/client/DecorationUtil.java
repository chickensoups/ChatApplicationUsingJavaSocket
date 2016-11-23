/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 *
 * @author VuongKM
 */
public class DecorationUtil {

    public static void setComponentNiceBorder(JPanel panel, String title) {
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border border = BorderFactory.createCompoundBorder(
                raisedbevel, loweredbevel);
        border = BorderFactory.createTitledBorder(
                border, title,
                TitledBorder.CENTER,
                TitledBorder.TOP);
        panel.setBorder(border);
    }
}
