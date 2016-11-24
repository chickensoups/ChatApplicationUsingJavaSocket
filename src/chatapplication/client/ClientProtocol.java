/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.client;

import chatapplication.execute.Client;
import chatapplication.client.component.MessageElement;
import chatapplication.client.component.RoomElement;
import chatapplication.client.util.DecorationUtil;
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
            client.currentUser.name = loginR.userName;

            client.loginPanel.loggedIn();
            DecorationUtil.enableRecursive(client.roomListPanel);
        }
        MessageElement messageElement = new MessageElement(client.serverUser, response.toString());
        client.serverMessagePanel.contentPanel.add(messageElement);
        client.serverMessagePanel.validate();
        client.serverMessagePanel.repaint();
    }

    public void processLogoutResponse(Response response) {
        LogoutR logoutR = (LogoutR) response;// TODO: dont know which going on here, just do signout logic
        client.currentUser.name = "";
        client.currentUser.currentRoom = null;

        client.loginPanel.loggedOut();
        DecorationUtil.disableRecursive(client.roomListPanel);
        DecorationUtil.disableRecursive(client.inRoomPanel);
    }

    public void processListRoomResponse(Response response) {
        ListRoomR listRoomR = (ListRoomR) response;

        client.roomListPanel.removeAll();
        for (Room room : listRoomR.rooms) {
            if (!client.rooms.contains(room)) {
                RoomElement roomElement = new RoomElement(room);
                DecorationUtil.addComponentAndRepaint(client.roomListPanel.roomListContentPanel, roomElement);
            }
        }
    }

    public void processCreateRoomResponse(Response response) {
        CreateRoomR createRoomR = (CreateRoomR) response;
        // Add room to list room
        client.rooms.add(createRoomR.room);
        // Add room element
        RoomElement roomElement = new RoomElement(createRoomR.room);
        DecorationUtil.addComponentAndRepaint(client.roomListPanel.roomListContentPanel, roomElement);
    }

    public void processJoinRoomResponse(Response response) {

    }

    public void processLeaveRoomResponse(Response response) {

    }

    public void processSendMessageResponse(Response response) {

    }
}
