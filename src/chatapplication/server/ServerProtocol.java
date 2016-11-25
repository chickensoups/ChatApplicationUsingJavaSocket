/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.server;

import chatapplication.response.CreateRoomR;
import chatapplication.response.JoinRoomR;
import chatapplication.response.LeaveRoomR;
import chatapplication.response.ListRoomR;
import chatapplication.response.LoginR;
import chatapplication.response.LogoutR;
import chatapplication.response.Response;
import chatapplication.response.SendMessageR;
import chatapplication.command.Command;
import chatapplication.command.CreateRoom;
import chatapplication.command.JoinRoom;
import chatapplication.command.LeaveRoom;
import chatapplication.command.ListRoom;
import chatapplication.command.Login;
import chatapplication.command.Logout;
import chatapplication.command.SendMessage;
import chatapplication.entity.User;
import chatapplication.util.Config;
import chatapplication.server.util.MessageUtil;
import chatapplication.server.util.NotificationUtil;
import chatapplication.server.util.RoomUtil;
import chatapplication.server.util.UserUtil;

/**
 *
 * @author VuongKM
 */
public class ServerProtocol {

    public Response processCommand(Command command) {
        Response response = null;
        if (command.name.equals(Config.Command.LOGIN.toString())) {
            response = processLogin(command);
        } else if (command.name.equals(Config.Command.LOGOUT.toString())) {
            response = processLogout(command);
        } else if (command.name.equals(Config.Command.LIST_ROOM.toString())) {
            response = processListRoom(command);
        } else if (command.name.equals(Config.Command.CREATE_ROOM.toString())) {
            response = processCreateRoom(command);
        } else if (command.name.equals(Config.Command.JOIN_ROOM.toString())) {
            response = processJoinRoom(command);
        } else if (command.name.equals(Config.Command.LEAVE_ROOM.toString())) {
            response = processLeaveRoom(command);
        } else if (command.name.equals(Config.Command.SEND_MESSAGE.toString())) {
            response = processSendMessage(command);
        }
        return response;
    }

    private Response processLogin(Command command) {
        Login login = (Login) command;
        LoginR loginR = new LoginR();
        loginR.name = login.name + Config.responseSign;
        loginR.userName = login.creator.name;

        if (login.creator.name.isEmpty()) {
            loginR.status = 400;
            loginR.message = "Username can't be empty!";
            return loginR;
        }

        if (UserUtil.isUserExisted(login.creator.name)) {
            loginR.status = 400;
            loginR.message = "Existed username";
            return loginR;
        }

        // Log user to server
        User loggedUser = UserUtil.logUserIn(login);
        // Notice
        NotificationUtil.noticeUser(loggedUser, loginR);

        return loginR;
    }

    private Response processLogout(Command command) {
        Logout logout = (Logout) command;
        LogoutR logoutR = new LogoutR();
        logoutR.name = logout.name + Config.responseSign;

        // Log user out
        User loggedOutUser = UserUtil.logUserOut(logout);
        // Notice
        NotificationUtil.noticeUser(loggedOutUser, logoutR);

        return logoutR;
    }

    private Response processListRoom(Command command) {
        ListRoom listRoom = (ListRoom) command;
        ListRoomR listRoomR = new ListRoomR();
        listRoomR.name = listRoom.name + Config.responseSign;
        listRoomR.rooms = RoomUtil.getListRoom();

        // Find user
        User user = UserUtil.findUser(listRoom.creator);
        // Notice to user
        NotificationUtil.noticeUser(user, listRoomR);

        return listRoomR;
    }

    private Response processCreateRoom(Command command) {
        CreateRoom createRoom = (CreateRoom) command;
        CreateRoomR createRoomR = new CreateRoomR();
        createRoomR.name = createRoom.name + Config.responseSign;

        if (createRoom.roomName.isEmpty()) {
            createRoomR.status = 400;
            createRoomR.message = "Room name can't be empty!";
            return createRoomR;
        }
        if (RoomUtil.isRoomExisted(createRoom.roomName)) {
            createRoomR.status = 400;
            createRoomR.message = "Existed room name";
            return createRoomR;
        }

        // Create room
        createRoomR.room = RoomUtil.createRoom(createRoom);
        // Notice all user
        NotificationUtil.noticeAllUser(createRoomR);

        return createRoomR;
    }

    private Response processJoinRoom(Command command) {
        JoinRoom joinRoom = (JoinRoom) command;
        JoinRoomR joinRoomR = new JoinRoomR();
        joinRoomR.name = joinRoom.name + Config.responseSign;
        joinRoomR.room = joinRoom.room;

        // Check if room is full
        if (RoomUtil.isRoomFull(joinRoom.room)) {
            joinRoomR.status = 400;
            joinRoomR.message = "Room is full";
            return joinRoomR;
        }

        // Add user to room
        UserUtil.joinRoom(joinRoom);

        // Notice other user in room
        NotificationUtil.noticeUserInRoom(joinRoom.room, joinRoomR);

        return joinRoomR;
    }

    private Response processLeaveRoom(Command command) {
        LeaveRoom leaveRoom = (LeaveRoom) command;
        LeaveRoomR leaveRoomR = new LeaveRoomR();
        leaveRoomR.name = leaveRoom.name + Config.responseSign;
        leaveRoomR.room = leaveRoom.room;

        // Leave room
        UserUtil.leaveRoom(leaveRoom);

        // Notice other user in room
        NotificationUtil.noticeUserInRoom(leaveRoomR.room, leaveRoomR);

        return leaveRoomR;
    }

    private Response processSendMessage(Command command) {
        SendMessage sendMessage = (SendMessage) command;
        SendMessageR sendMessageR = new SendMessageR();
        sendMessageR.name = sendMessage.name + Config.responseSign;
        sendMessageR.content = sendMessage.content;

        // Check if message is empty
        if (sendMessage.content.isEmpty()) {
            sendMessageR.status = 400;
            sendMessageR.message = "Message can't be empty!";
            return sendMessageR;
        }

        // Check if message is valid or not
        if (!MessageUtil.isValid(sendMessage)) {
            sendMessageR.status = 400;
            sendMessageR.message = "Message is not valid!";
            return sendMessageR;
        }

        // Send message
        MessageUtil.sendMessage(sendMessage);

        // Notice other user in room
        NotificationUtil.noticeUserInRoom(sendMessage.creator.currentRoom, sendMessageR);

        return sendMessageR;
    }
}
