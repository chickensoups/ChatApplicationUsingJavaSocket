/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.server;

import chatapplication.response.Response;
import chatapplication.command.Command;
import chatapplication.command.Login;
import chatapplication.entity.User;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VuongKM
 */
public class Handler extends Thread {

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private ServerProtocol protocol;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            protocol = new ServerProtocol();
            // Login using unique username
            while (true) {
                Command command = (Command) in.readObject();
                if (command != null) {
                    if (command instanceof Login) {
                        command.creator.out = out;
                        command.creator.in = in;
                    }
                    Response response = protocol.processCommand(command);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
