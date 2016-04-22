/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatSystem;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Swashy
 */
public class Chat {

    private static final int PORT = 9006;
    private static String SERVER = "Localhost";
    private Socket clientSocket;

    public void clientConnect(String server, int port) {

        BufferedReader in = null;
        PrintWriter out = null;

        try {
            System.out.println("Attempting to connect to " + SERVER + ":" + PORT);
            clientSocket = new Socket(server, port);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            System.out.println("Connecion succeed");

        } catch (IOException ex) {
            System.out.println("Failed to connect to chat, is the chat server up?");
        }

    }

}
