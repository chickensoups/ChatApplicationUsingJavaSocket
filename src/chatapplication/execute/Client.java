/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.execute;

import chatapplication.client.ClientProtocol;
import chatapplication.client.component.InRoomPanel;
import chatapplication.client.component.LoginPanel;
import chatapplication.client.util.RandomIDUtil;
import chatapplication.client.util.DecorationUtil;
import chatapplication.client.component.RoomListPanel;
import chatapplication.client.component.ServerMessagePanel;
import chatapplication.response.Response;
import chatapplication.command.CreateRoom;
import chatapplication.command.ListRoom;
import chatapplication.command.Login;
import chatapplication.command.Logout;
import chatapplication.command.SendMessage;
import chatapplication.entity.Room;
import chatapplication.entity.User;
import chatapplication.util.Config;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 *
 * @author VuongKM
 */
public class Client {

    public ObjectOutputStream out;
    public ObjectInputStream in;
    public ClientProtocol clientProtocol;
    public User currentUser;
    public HashSet<Room> rooms = new HashSet<Room>();
    public JFrame frame;

    // Server message panel
    public ServerMessagePanel serverMessagePanel;
    // Login panel component
    public LoginPanel loginPanel;
    // List room component
    public RoomListPanel roomListPanel;
    // In room component
    public InRoomPanel inRoomPanel;// Panel for in room chat
    // Init default server user
    public User serverUser;
    public String uniqueId;

    public Client() {
        // Init main frame
        frame = new JFrame("Chat application - java native socket");
        frame.setLayout(new BorderLayout());

        // Init user on client
        uniqueId = RandomIDUtil.nextSessionId();
        currentUser = new User(uniqueId, uniqueId);

        // Init server user
        serverUser = new User(uniqueId, "Server");

        // Server message panel
        serverMessagePanel = new ServerMessagePanel();
        frame.add(serverMessagePanel, BorderLayout.WEST);

        // Login, logout panel
        loginPanel = new LoginPanel();
        loginPanel.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    currentUser = new User(uniqueId, loginPanel.usernameTextField.getText());
                    Login login = new Login(currentUser);
                    out.writeObject(login);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        loginPanel.logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Logout logout = new Logout(currentUser);
                    out.writeObject(logout);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        frame.add(loginPanel, BorderLayout.NORTH);

        // Room list panel
        roomListPanel = new RoomListPanel();
        roomListPanel.createRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CreateRoom createRoom = new CreateRoom(currentUser, roomListPanel.roomNameTextField.getText());
                    out.writeObject(createRoom);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        frame.add(roomListPanel, BorderLayout.EAST);
        DecorationUtil.disableRecursive(roomListPanel);

        // In room panel
        inRoomPanel = new InRoomPanel();
        inRoomPanel.messageTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SendMessage sendMessage = new SendMessage(currentUser, inRoomPanel.messageTextField.getText());
                    out.writeObject(sendMessage);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        frame.add(inRoomPanel, BorderLayout.CENTER);
        DecorationUtil.disableRecursive(inRoomPanel);

        // Pack the frame
        frame.pack();
    }

    public void start() {
        try {
            Socket socket = new Socket(Config.address, Config.port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            // Process all messages from server, according to the protocol.
            clientProtocol = new ClientProtocol(this);

            while (true) {
                Response response = (Response) in.readObject();
                if (response != null) {
                    clientProtocol.processResponse(response);
                }
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
