/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatSystem;

import controllers.FXMLDocumentSecondScene;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

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
    public void sendMessage(){
            try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            Pane p = fxmlLoader.load(getClass().getResource("/GameLayouts/FXMLDocumentSecondScene.fxml").openStream());
            FXMLDocumentSecondScene controller = (FXMLDocumentSecondScene) fxmlLoader.getController();
            
            //använd controllern sen för att FXML ska kunna prata med java filer
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
