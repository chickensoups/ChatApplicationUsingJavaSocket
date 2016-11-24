/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client;

import chatapplication.Response.CreateRoomR;
import chatapplication.Response.JoinRoomR;
import chatapplication.Response.LeaveRoomR;
import chatapplication.Response.ListRoomR;
import chatapplication.Response.LoginR;
import chatapplication.Response.LogoutR;
import chatapplication.Response.Response;
import chatapplication.Response.SendMessageR;
import chatapplication.command.CreateRoom;
import chatapplication.command.Login;
import chatapplication.command.Logout;
import chatapplication.command.SendMessage;
import chatapplication.entity.Room;
import chatapplication.entity.User;
import chatapplication.util.Config;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 *
 * @author VuongKM
 */
public class Client {

    ObjectOutputStream out;
    ObjectInputStream in;
    User currentUser;
    HashSet<Room> rooms = new HashSet<Room>();

    JFrame frame = new JFrame("Awesome chat application");

    // Messge from server panel component
    JPanel messageFromServerPanel = new JPanel();
    JPanel messageFromServerContentPanel = new JPanel();

    // Login panel component
    JPanel loginPanel = new JPanel();
    JTextField usernameTextField = new JTextField(50);
    JButton loginButton = new JButton("Login");
    JButton logoutButton = new JButton("Logout");

    // List room component
    JPanel listRoomPanel = new JPanel(); // Panel for room list
    JPanel createRoomFormPanel = new JPanel(); // Panel for create room components
    JTextField roomNameTextField = new JTextField(30);
    JButton createRoomButton = new JButton("Create Room");

    JPanel listRoomContentPanel = new JPanel();

    // In room component
    JPanel inRoomPanel = new JPanel(); // Panel for in room chat
    JTextField messageTextField = new JTextField(50);
    JPanel inRoomContentPanel = new JPanel();
    JPanel chatArea = new JPanel();

    // Init default server user
    User serverUser = new User(RandomID.nextSessionId(), "Creator");

    Room defaultRoom = null;

    public void initMessageFromServerPanel() {
        messageFromServerPanel.setSize(200, 5000);
        messageFromServerContentPanel.setLayout(new GridLayout(10, 1));
        messageFromServerPanel.add(new JScrollPane(messageFromServerContentPanel), "Center");

        MessageElement element = new MessageElement(serverUser, "Server message will be show bellow!");
        messageFromServerContentPanel.add(element);
        DecorationUtil.setComponentNiceBorder(messageFromServerPanel, "Server Message");
    }

    public void initLoginPanel() {
        DecorationUtil.setComponentNiceBorder(loginPanel, "Login form");
        loginPanel.add(usernameTextField);
        loginPanel.add(loginButton);
        loginPanel.add(logoutButton);

        usernameTextField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (usernameTextField.getText().isEmpty()) {
                    loginButton.setEnabled(false);
                } else {
                    loginButton.setEnabled(true);
                }
            }
        });

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Login login = new Login(currentUser.id, usernameTextField.getText());
                    out.writeObject(login);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        logoutButton.addActionListener(new ActionListener() {
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

        loginButton.setEnabled(false);
        logoutButton.setEnabled(false);
    }

    public void initListRoomPanel() {
        createRoomFormPanel.setLayout(new FlowLayout());
        createRoomFormPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        createRoomFormPanel.add(roomNameTextField);
        createRoomFormPanel.add(createRoomButton);

        createRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    CreateRoom createRoom = new CreateRoom(currentUser, roomNameTextField.getText());
                    out.writeObject(createRoom);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        listRoomContentPanel.setLayout(new GridLayout(10, 1));
        RoomElement roomElement = new RoomElement(defaultRoom);
        listRoomContentPanel.add(roomElement);

        listRoomPanel.setLayout(new BorderLayout(2, 1));
        listRoomPanel.add(createRoomFormPanel, BorderLayout.PAGE_START);
        listRoomPanel.add(new JScrollPane(listRoomContentPanel), BorderLayout.CENTER);
        listRoomPanel.setEnabled(false);
        DecorationUtil.setComponentNiceBorder(listRoomPanel, "Room list");
    }

    public void initInRoomPanel() {
        inRoomPanel.setLayout(new BorderLayout());
        inRoomContentPanel.add(chatArea);
        inRoomPanel.add(new JScrollPane(inRoomContentPanel), BorderLayout.CENTER);
        inRoomPanel.add(messageTextField, BorderLayout.PAGE_END);

        // Add Listeners
        messageTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    SendMessage sendMessage = new SendMessage(currentUser, messageTextField.getText());
                    out.writeObject(sendMessage);
                } catch (IOException ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        DecorationUtil.setComponentNiceBorder(inRoomPanel, "Chat room");
        inRoomPanel.setEnabled(false);
    }

    public Client() {
        String randomId = RandomID.nextSessionId();
        currentUser = new User(randomId, randomId);
        initLoginPanel();
        initMessageFromServerPanel();
        initListRoomPanel();
        initInRoomPanel();
        frame.setLayout(new BorderLayout());

        frame.add(loginPanel, BorderLayout.NORTH);
        frame.add(messageFromServerPanel, BorderLayout.WEST);

        // Default disable list room and in room panel
        DecorationUtil.disableRecursive(listRoomPanel);
        DecorationUtil.disableRecursive(inRoomPanel);

        frame.add(inRoomPanel, BorderLayout.CENTER);
        frame.add(listRoomPanel, BorderLayout.EAST);
        frame.pack();
    }

    public void processResponse(Response response) {
        if (response instanceof LoginR) {
            processLoginResponse(response);
        } else if (response instanceof ListRoomR) {
            processListRoomResponse(response);
        } else if (response instanceof LogoutR) {
            processLogoutResponse(response);
        } else if (response instanceof CreateRoomR) {
            processCreateRoomResponse(response);
        } else if (response instanceof JoinRoomR) {
            processJoinRoomResponse(response);
        } else if (response instanceof LeaveRoomR) {
            processLeaveRoomResponse(response);
        } else if (response instanceof SendMessageR) {
            processSendMessageResponse(response);
        }
    }

    public void processLoginResponse(Response response) {
        LoginR loginR = (LoginR) response;
        if (response.status == 200) {
            currentUser = new User();
            currentUser.name = loginR.userName;

            loginButton.setEnabled(false);
            usernameTextField.setEnabled(false);
            logoutButton.setEnabled(true);

            DecorationUtil.enableRecursive(listRoomPanel);
        }
        MessageElement messageElement = new MessageElement(serverUser, response.toString());
        DecorationUtil.addComponentAndRepain(messageFromServerContentPanel, messageElement);
    }

    public void processLogoutResponse(Response response) {
        LogoutR logoutR = (LogoutR) response;// TODO: dont know which going on here, just do signout logic
        currentUser.name = "";
        currentUser.currentRoom = null;

        logoutButton.setEnabled(false);
        loginButton.setEnabled(true);
        usernameTextField.setEnabled(true);

        DecorationUtil.disableRecursive(listRoomPanel);
        DecorationUtil.disableRecursive(inRoomPanel);
    }

    public void processListRoomResponse(Response response) {
        ListRoomR listRoomR = (ListRoomR) response;

        listRoomContentPanel.removeAll();
        for (Room room : listRoomR.rooms) {
            if (!rooms.contains(room)) {
                RoomElement roomElement = new RoomElement(room);
                DecorationUtil.addComponentAndRepain(listRoomContentPanel, roomElement);
            }
        }
    }

    public void processCreateRoomResponse(Response response) {
        CreateRoomR createRoomR = (CreateRoomR) response;
        // Add room to list room
        rooms.add(createRoomR.room);
        // Add room element
        RoomElement roomElement = new RoomElement(createRoomR.room);
        DecorationUtil.addComponentAndRepain(listRoomContentPanel, roomElement);
    }

    public void processJoinRoomResponse(Response response) {

    }

    public void processLeaveRoomResponse(Response response) {

    }

    public void processSendMessageResponse(Response response) {

    }

    public void start() {
        try {
            Socket socket = new Socket(Config.address, Config.port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            // Process all messages from server, according to the protocol.

            while (true) {
                Response response = (Response) in.readObject();
                if (response != null) {
                    processResponse(response);
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
