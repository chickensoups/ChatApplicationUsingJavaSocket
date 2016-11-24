/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.execute;

import chatapplication.entity.Room;
import chatapplication.entity.User;
import chatapplication.server.Handler;
import chatapplication.util.Config;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VuongKM
 */
public class Server {

    public static HashSet<User> users = new HashSet<User>();
    public static HashSet<Room> rooms = new HashSet<Room>();

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(Config.port);
            System.out.println("Server is running at `" + Config.address + ":" + Config.port + "`");
            while (true) {
                new Handler(serverSocket.accept()).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
