/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.protocol;

import chatapplication.Response.CreateRoomR;
import chatapplication.Response.JoinRoomR;
import chatapplication.Response.LeaveRoomR;
import chatapplication.Response.ListRoomR;
import chatapplication.Response.LoginR;
import chatapplication.Response.LogoutR;
import chatapplication.Response.Response;
import chatapplication.Response.SendMessageR;
import chatapplication.command.Command;
import chatapplication.command.CreateRoom;
import chatapplication.command.JoinRoom;
import chatapplication.command.LeaveRoom;
import chatapplication.command.ListRoom;
import chatapplication.command.Login;
import chatapplication.command.Logout;
import chatapplication.command.SendMessage;
import chatapplication.entity.Room;
import chatapplication.entity.User;
import chatapplication.server.Server;
import chatapplication.util.Config;
import chatapplication.util.MessageUtil;
import chatapplication.util.NotificationUtil;
import chatapplication.util.RoomUtil;
import chatapplication.util.UserUtil;

/**
 *
 * @author VuongKM
 */
public class Protocol {

    public Response processCommand(Command command) {
        Response response = null;
        if (command.name.equals(Config.Command.LOGIN.toString())) {
            response = processLogin(command);
        } else if (command.name.equals(Config.Command.LOGOUT.toString())) {
            response = processLogout(command);
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
        loginR.userName = login.userName;

        if (login.userName.isEmpty()) {
            loginR.status = 400;
            loginR.message = "Username can't be empty!";
            return loginR;
        }

        if (UserUtil.isUserExisted(login.userName)) {
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

        UserUtil.logUserOut(logout);
        return logoutR;
    }

    private Response processListRoom(Command command) {
        ListRoom listRoom = (ListRoom) command;
        ListRoomR listRoomR = new ListRoomR();
        listRoomR.name = listRoom.name + Config.responseSign;
        listRoomR.rooms = RoomUtil.getListRoom();

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
