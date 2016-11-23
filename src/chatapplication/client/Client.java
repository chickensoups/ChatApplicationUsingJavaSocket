/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client;

import chatapplication.Response.Response;
import chatapplication.command.Login;
import chatapplication.command.SendMessage;
import chatapplication.entity.User;
import chatapplication.util.Config;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author VuongKM
 */
public class Client {

    ObjectOutputStream out;
    ObjectInputStream in;
    User currentUser;

    JFrame frame = new JFrame("Awesome chat application");

    // Messge from server panel component
    JPanel messageFromServerPanel = new JPanel();

    JPanel mainPanel = new JPanel();

    // Login panel component
    JPanel loginPanel = new JPanel();
    JTextField usernameTextField = new JTextField(50);
    JButton loginButton = new JButton("Login");
    JButton logoutButton = new JButton("Logout");

    // List room component
    JPanel listRoomPanel = new JPanel(); // Panel for room list

    // In room component
    JPanel inRoomPanel = new JPanel(); // Panel for in room chat
    JTextField messageTextField = new JTextField(50);
    JPanel chatArea = new JPanel();

    public void initMessageFromServerPanel() {
        DecorationUtil.setComponentNiceBorder(messageFromServerPanel, "Server Message");
    }

    public void initLoginPanel() {
        DecorationUtil.setComponentNiceBorder(loginPanel, "Login form");
        loginPanel.add(usernameTextField);
        loginPanel.add(loginButton);
        loginPanel.add(logoutButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Login login = new Login();
                    login.userName = usernameTextField.getText();
                    out.writeObject(login);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logout logic
            }
        });
    }

    public void initListRoomPanel() {
        DecorationUtil.setComponentNiceBorder(listRoomPanel, "Room list");
        listRoomPanel.setSize(200, 600);
        listRoomPanel.setEnabled(false);
    }

    public void initInRoomPanel() {
        DecorationUtil.setComponentNiceBorder(inRoomPanel, "Chat room");
        inRoomPanel.add(messageTextField);
        inRoomPanel.add(chatArea);

        chatArea.setSize(700, 500);
        // Add Listeners
        messageTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.content = messageTextField.getText();
                    sendMessage.name = Config.Command.SEND_MESSAGE.toString();
                    sendMessage.creator = currentUser;
                    out.writeObject(sendMessage);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        inRoomPanel.setEnabled(false);
    }

    public void initMainPanel() {
        initLoginPanel();
        initListRoomPanel();
        initInRoomPanel();

        mainPanel.add(loginPanel);
        mainPanel.add(listRoomPanel);
        mainPanel.add(inRoomPanel);
        mainPanel.setSize(700, 600);
    }

    public Client() {
        frame.setLayout(new GridLayout(2, 2));
        frame.add(messageFromServerPanel);
        initMainPanel();
        frame.add(mainPanel);
        frame.setSize(1000, 700);
        frame.pack();
    }

    public void start() {
        try {
            Socket socket = new Socket(Config.address, Config.port);
//            Protocol protocol = new Protocol();
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            // Process all messages from server, according to the protocol.
            while (true) {
                Response response = (Response) in.readObject();
                System.out.println(response);
            }
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.start();
    }
}
