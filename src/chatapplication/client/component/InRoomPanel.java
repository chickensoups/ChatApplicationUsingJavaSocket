/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client.component;

import chatapplication.client.util.DecorationUtil;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author VuongKM
 */
public class InRoomPanel extends JPanel {

    public JTextField messageTextField;
    public JPanel inRoomContentPanel;
    public JPanel chatArea;

    public InRoomPanel() {
        messageTextField = new JTextField(50);
        inRoomContentPanel = new JPanel();
        chatArea = new JPanel();
        
        setLayout(new BorderLayout());
        inRoomContentPanel.add(chatArea);
        add(new JScrollPane(inRoomContentPanel), BorderLayout.CENTER);
        add(messageTextField, BorderLayout.PAGE_END);

        DecorationUtil.setComponentNiceBorder(this, "Chat room");
    }
}
