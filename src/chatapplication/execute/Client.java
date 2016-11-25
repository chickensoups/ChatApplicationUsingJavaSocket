/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.execute;

import chatapplication.client.ClientProtocol;
import chatapplication.client.component.InRoomPanel;
import chatapplication.client.component.LoginPanel;
import chatapplication.client.component.LogoutPanel;
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
    public HashSet<Room> rooms;
    public JFrame frame;

    // Server message panel
    public ServerMessagePanel serverMessagePanel;
    // Login panel
    public LoginPanel loginPanel;
    // Logout panel
    public LogoutPanel logoutPanel;
    // List room
    public RoomListPanel roomListPanel;
    // In room
    public InRoomPanel inRoomPanel;// Panel for in room chat
    // Init default server user
    public User serverUser;
    public String uniqueId;

    public Client() {
        // Init main frame
        frame = new JFrame("Chat application using Java's native socket");
        frame.setLayout(new BorderLayout());

        // Init rooms
        rooms = new HashSet<>();

        // Init user on client
        uniqueId = RandomIDUtil.nextSessionId();
        currentUser = new User(uniqueId, uniqueId);

        // Init server user
        serverUser = new User(uniqueId, "Server");

        // Server message panel
        serverMessagePanel = new ServerMessagePanel();
        frame.add(serverMessagePanel, BorderLayout.WEST);

        // Login panel
        loginPanel = new LoginPanel(this);
        frame.add(loginPanel, BorderLayout.NORTH);

        // Room list panel
        roomListPanel = new RoomListPanel(this);
        frame.add(roomListPanel, BorderLayout.EAST);
        DecorationUtil.disableRecursive(roomListPanel);

        // In room panel
        inRoomPanel = new InRoomPanel(this);
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
