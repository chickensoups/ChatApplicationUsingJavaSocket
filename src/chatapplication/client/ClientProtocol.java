/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client;

import chatapplication.client.component.LoginPanel;
import chatapplication.client.component.LogoutPanel;
import chatapplication.execute.Client;
import chatapplication.client.component.MessageElement;
import chatapplication.client.component.RoomElement;
import chatapplication.client.util.DecorationUtil;
import chatapplication.command.JoinRoom;
import chatapplication.command.ListRoom;
import chatapplication.entity.Room;
import chatapplication.entity.User;
import chatapplication.response.CreateRoomR;
import chatapplication.response.JoinRoomR;
import chatapplication.response.LeaveRoomR;
import chatapplication.response.ListRoomR;
import chatapplication.response.LoginR;
import chatapplication.response.LogoutR;
import chatapplication.response.Response;
import chatapplication.response.SendMessageR;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VuongKM
 */
public class ClientProtocol {

    public Client client;

    public ClientProtocol(Client client) {
        this.client = client;
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
            client.currentUser = new User();
            client.currentUser.id = client.uniqueId;
            client.currentUser.name = loginR.userName;

            MessageElement messageElement = new MessageElement(client.serverUser, response.toString());
            DecorationUtil.addComponentAndRepaint(client.serverMessagePanel.contentPanel, messageElement);

            client.frame.remove(client.loginPanel);
            client.logoutPanel = new LogoutPanel(client);
            client.frame.add(client.logoutPanel, BorderLayout.NORTH);
            client.frame.validate();
            client.frame.repaint();

            DecorationUtil.enableRecursive(client.roomListPanel);
            try {
                // Get list room first time
                ListRoom listRoom = new ListRoom(client.currentUser);
                client.out.writeObject(listRoom);
            } catch (IOException ex) {
                Logger.getLogger(ClientProtocol.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void processLogoutResponse(Response response) {
        LogoutR logoutR = (LogoutR) response;// TODO: dont know which going on here, just do signout logic
        client.currentUser.name = "";
        client.currentUser.currentRoom = null;

        client.frame.remove(client.logoutPanel);
        client.loginPanel = new LoginPanel(client);
        client.frame.add(client.loginPanel, BorderLayout.NORTH);
        client.frame.validate();
        client.frame.repaint();
        DecorationUtil.disableRecursive(client.roomListPanel);
        DecorationUtil.disableRecursive(client.inRoomPanel);
    }

    public void processListRoomResponse(Response response) {
        ListRoomR listRoomR = (ListRoomR) response;

        client.roomListPanel.roomListContentPanel.removeAll();
        if (listRoomR.rooms.size() <= 0) {
            client.roomListPanel.noRoomMessage.setVisible(true);
        } else {
            client.roomListPanel.noRoomMessage.setVisible(false);
            for (Room room : listRoomR.rooms) {
                if (!client.rooms.contains(room)) {
                    RoomElement roomElement = new RoomElement(room);
                    roomElement.join.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            try {
                                JoinRoom joinRoom = new JoinRoom(client.currentUser, room);
                                client.out.writeObject(joinRoom);
                            } catch (IOException ex) {
                                Logger.getLogger(ClientProtocol.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    DecorationUtil.addComponentAndRepaint(client.roomListPanel.roomListContentPanel, roomElement);
                }
            }
        }
    }

    public void processCreateRoomResponse(Response response) {
        CreateRoomR createRoomR = (CreateRoomR) response;
        if (createRoomR.status == 400) {
            MessageElement messageElement = new MessageElement(client.serverUser, createRoomR.message);
            DecorationUtil.addComponentAndRepaint(client.serverMessagePanel.contentPanel, messageElement);
        } else {
            // Add room to list room
            client.rooms.add(createRoomR.room);
            // Add room element
            client.roomListPanel.noRoomMessage.setVisible(false);
            RoomElement roomElement = new RoomElement(createRoomR.room);
            roomElement.join.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        JoinRoom joinRoom = new JoinRoom(client.currentUser, createRoomR.room);
                        client.out.writeObject(joinRoom);
                    } catch (IOException ex) {
                        Logger.getLogger(ClientProtocol.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            DecorationUtil.addComponentAndRepaint(client.roomListPanel.roomListContentPanel, roomElement);
        }
    }

    public void processJoinRoomResponse(Response response) {
        JoinRoomR joinRoomR = (JoinRoomR) response;

        // Find room
        client.currentUser.currentRoom = joinRoomR.room;
        // Enable in room panel
        DecorationUtil.enableRecursive(client.inRoomPanel);
        // Refresh list room
        for (Component roomComponent : client.roomListPanel.roomListContentPanel.getComponents()) {
            if (roomComponent instanceof RoomElement) {
                RoomElement roomElement = (RoomElement) roomComponent;
                if (roomElement.roomName.getText().equals(joinRoomR.room.name)) {
                    roomElement.userCount.setText(joinRoomR.room.userCount + "");
                }
            }
        }
        // Add welcome message to chat panel
        MessageElement messageElement;
        if (joinRoomR.user.id.equals(client.currentUser.id)) {
            client.currentUser.currentRoom = joinRoomR.room;
            messageElement = new MessageElement(client.serverUser, "Welcome " + joinRoomR.user.name + ", have fun!");
            DecorationUtil.disableRecursive(client.roomListPanel);
        } else {
            messageElement = new MessageElement(client.serverUser, joinRoomR.user.name + " had joined");
        }
        DecorationUtil.addComponentAndRepaint(client.inRoomPanel.chatArea, messageElement);

    }

    public void processLeaveRoomResponse(Response response) {
        LeaveRoomR leaveRoomR = (LeaveRoomR) response;

        // Add welcome message to chat panel
        if (!leaveRoomR.user.id.equals(client.currentUser.id)) {
            MessageElement messageElement = new MessageElement(client.serverUser, leaveRoomR.user.name + " had leave");
            DecorationUtil.addComponentAndRepaint(client.inRoomPanel.chatArea, messageElement);
        }
        DecorationUtil.disableRecursive(client.inRoomPanel);
        DecorationUtil.enableRecursive(client.roomListPanel);

        // Refresh list room
        for (Component roomComponent : client.roomListPanel.roomListContentPanel.getComponents()) {
            if (roomComponent instanceof RoomElement) {
                RoomElement roomElement = (RoomElement) roomComponent;
                if (roomElement.roomName.getText().equals(leaveRoomR.room.name)) {
                    roomElement.userCount.setText(leaveRoomR.room.userCount + "");
                }
            }
        }
    }

    public void processSendMessageResponse(Response response) {
        SendMessageR sendMessageR = (SendMessageR) response;
        // Add welcome message to chat panel
        MessageElement messageElement;
        if (sendMessageR.user.id.equals(client.currentUser.id)) {
            messageElement = new MessageElement(new User(client.uniqueId, "You"), sendMessageR.content);
        } else {
            messageElement = new MessageElement(sendMessageR.user, sendMessageR.content);
        }
        DecorationUtil.addComponentAndRepaint(client.inRoomPanel.chatArea, messageElement);
    }
}
