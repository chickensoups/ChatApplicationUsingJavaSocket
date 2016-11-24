/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client.component;

import chatapplication.client.util.DecorationUtil;
import chatapplication.entity.User;
import java.awt.Graphics;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author VuongKM
 */
public class ServerMessagePanel extends JPanel {

    public JPanel contentPanel;

    public ServerMessagePanel() {
        contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(10, 1));
        add(new JScrollPane(contentPanel), "Center");
        MessageElement element = new MessageElement(new User("abc", "abc"), "Server message will be show bellow!");
        contentPanel.add(element);
        DecorationUtil.setComponentNiceBorder(this, "Server Message");
    }
}
