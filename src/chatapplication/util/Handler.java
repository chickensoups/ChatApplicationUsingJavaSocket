/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.util;

import chatapplication.Response.LoginR;
import chatapplication.Response.Response;
import chatapplication.command.Command;
import chatapplication.command.Login;
import chatapplication.entity.User;
import chatapplication.protocol.Protocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VuongKM
 */
public class Handler extends Thread {

    private String name;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private Protocol protocol;
    private User user;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            protocol = new Protocol();
            // Login using unique username
            while (true) {
                Command command = (Command) in.readObject();

                if (command != null) {
                    Response response;
                    if (command instanceof Login) {
                        Login login = (Login) command;
                        user = UserUtil.logUserIn(login);
                        if (user != null) {
                            user.out = out;
                            user.in = in;
                        }
                        LoginR loginR = new LoginR();
                        loginR.name = login.name + Config.responseSign;
                        loginR.userName = login.userName;
                        response = loginR;
                        // Notice
                        NotificationUtil.noticeUser(user, loginR);
                    } else {
                        response = protocol.processCommand(command);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
