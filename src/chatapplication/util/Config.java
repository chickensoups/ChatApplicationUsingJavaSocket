/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatapplication.util;

/**
 *
 * @author VuongKM
 */
public class Config {

    public static int port = 3000;
    public static String address = "127.0.0.1";
    public static String responseSign = "__R";
    public static int maxUserPerRoom = 2;

    public static enum Command {
        LOGIN("LOGIN"),
        LOGOUT("LOGOUT"),
        LIST_ROOM("LIST_ROOM"),
        CREATE_ROOM("CREATE_ROOM"),
        JOIN_ROOM("JOINT_ROOM"),
        LEAVE_ROOM("LEAVE_ROOM"),
        SEND_MESSAGE("SEND_MESSAGE");

        private final String text;

        private Command(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
