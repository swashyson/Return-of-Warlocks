/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playerField;

import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Swashy
 */
public class BroadCastSystemForMasterIngame {

    static ArrayList broadCastList = new ArrayList();
    static ArrayList<Socket> clientSockets = new ArrayList<>();

    public static ArrayList getBroadCastList() {
        return broadCastList;
    }

    public static void setBroadCastList(ArrayList broadCastList) {
        BroadCastSystemForMasterIngame.broadCastList = broadCastList;
    }

    public static ArrayList<Socket> getClientSockets() {
        return clientSockets;
    }

    public static void setClientSockets(ArrayList<Socket> clientSockets) {
        BroadCastSystemForMasterIngame.clientSockets = clientSockets;
    }

}
