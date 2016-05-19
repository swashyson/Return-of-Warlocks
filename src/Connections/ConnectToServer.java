/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connections;


import dataStorage.DataStorage;
import java.io.IOException;
import java.net.Socket;
import controllers.FXMLloggInController;


/**
 *
 * @author Johan
 */


public class ConnectToServer {
    private static final int PORT = 9006;
    private static String SERVER = "Localhost";
    private Socket clientToServerSocket;
   public void connectToServer(String server, int port) {
        FXMLloggInController FXMLDC = new FXMLloggInController();
        try {
            clientToServerSocket = new Socket(server, port);
            DataStorage.setChatClientSocket(clientToServerSocket);

        } catch (IOException ex) {
            
        }

    } 
   
}
