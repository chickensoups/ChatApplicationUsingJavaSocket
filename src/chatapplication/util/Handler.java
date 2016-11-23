/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.util;

import chatapplication.command.Command;
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

    public Handler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            
            // Login using unique username
            while(true) {
                Command command = (Command) in.readObject();
                if(command != null) {
                    System.out.println(command);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Handler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
