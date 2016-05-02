/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatSystem;

import controllers.FXMLDocumentSecondScene;
import dataStorage.DataStorage;
import dataStorage.informationStorage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

/**
 *
 * @author Swashy
 */
public class Chat {

    private static final int PORT = 9006;
    private static String SERVER = "Localhost";
    private Socket clientSocket;

    public Chat() {

        saveServerInformation();
    }

    public String getServerIp(){
        return SERVER;
    }
    public void clientConnect(String server, int port) {

        try {
            System.out.println("Attempting to connect to " + SERVER + ":" + PORT);
            clientSocket = new Socket(server, port);
            
            System.out.println("Connecion succeed");

        } catch (IOException ex) {
            System.out.println("Failed to connect to chat, is the chat server up?");
        }

    }

    public final void saveServerInformation() {

        informationStorage.setServerIP(SERVER);
    }

    public void sendMessage(String message) {

        PrintWriter out = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(DataStorage.getUserName() + ": " + message);
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void checkForIncommingMessage() {

        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while (true) {
                String test = in.readLine();
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        if (test.contains("|||||")) {

                            playerNamesSplitterAndAdder();

                        } else {
                            DataStorage.getAllChat().appendText(test + "\n");

                        }

                    }

                    private void playerNamesSplitterAndAdder() {
                        
                        DataStorage.newObservableList();

                        System.out.println("DISPLAY NAME " + DataStorage.getPlayerListAdapter().size());

                        String name = test.substring(5);

                        String[] splitArray = name.split(",");
                        for (String s : splitArray) {
                            DataStorage.getPlayerListAdapter().add(s);
                        }
                        DataStorage.getPlayerList().setItems(DataStorage.getPlayerListAdapter());
                    }
                });

            }
        } catch (SocketException ex) {
            System.out.println("Lost connection");
        } catch (Exception ex2) {
            ex2.printStackTrace();
        }

    }

    public void sendNameToServer() {

        PrintWriter out = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("|||||" + DataStorage.getUserName());
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
