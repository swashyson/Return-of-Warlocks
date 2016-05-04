/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatSystem;

import dataStorage.DataStorage;
import dataStorage.PlayersStorage;
import dataStorage.informationStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Swashy
 */
public class LocalChatSlave {

    private static final int PORT = 9007;
    private static String SERVER = "Localhost";
    private Socket clientSocket;

    public LocalChatSlave() {

    }

    public void clientConnect(String server, int port) {

        try {
            //System.out.println("Attempting to connect to " + PlayersStorage.getMasterSocketIP().toString().replace("[", "").replace("]", "") + ":" + PlayersStorage.getMasterSocketPORTString().toString().replace("[", "").replace("]", ""));
            clientSocket = new Socket(PlayersStorage.getMasterSocketIP().replace("[", "").replace("]", ""), PORT);
            System.out.println("Connecion succeed" + PlayersStorage.getMasterSocketIP() + PORT);
            DataStorage.setLobbyClientSocket(clientSocket);

        } catch (IOException ex) {
            System.out.println("Failed to connect to chat, is the chat server up?");
            ex.printStackTrace();
        }

    }

    public final void saveServerInformation() {

        informationStorage.setServerIP(SERVER);
    }

    public void sendMessage(String message) {

        PrintWriter out = null;

        try {
            out = new PrintWriter(DataStorage.getLobbyClientSocket().getOutputStream(), true);
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

                        } else if (test.contains("||||&")) {

                            System.out.println("Added server");

                        } else {
                            DataStorage.getAllChat().appendText(test + "\n");

                        }

                    }

                    private void playerNamesSplitterAndAdder() {

                        PlayersStorage.getDisplayPlayerNamesFrames().clear();
                        String name = test.substring(5);
                        System.out.println("Slave Recieved name::" + name);
                        PlayersStorage.getDisplayPlayerNamesFrames().add(name.replace("[", "").replace("]", ""));
                        setPlayerFrames();
                    }
                });

            }
        } catch (SocketException ex) {
            System.out.println("Lost connection 1");
            handleSlaveDisconnect();
        } catch (Exception ex2) {

            ex2.printStackTrace();
        }

    }
    private void handleSlaveDisconnect(){
        System.out.println("disconnected");
    }

    public void sendNameToServer() {

        PrintWriter out = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("|||||" + DataStorage.getUserName());
            out.flush();
            System.out.println("Send name from Slave to master: " + DataStorage.getUserName());
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void disconnect(){
        
        try {
            DataStorage.getLobbyClientSocket().close();
            System.out.println("closing slave: "+DataStorage.getLobbyClientSocket());
        } catch (Exception ex) {
            Logger.getLogger(LocalChatSlave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void setPlayerFrames() {

        List<String> list = Arrays.asList(PlayersStorage.getDisplayPlayerNamesFrames().toString().split("\\s*,\\s*"));
        
        System.out.println("DEN FÖRSTA I LISTAN ÄR :: " + list.get(0));

        if (list.size() == 1) {
            PlayersStorage.getPlayer1().setText(list.get(0));
        } else if (list.size() == 2) {
            PlayersStorage.getPlayer1().setText(list.get(0));
            PlayersStorage.getPlayer2().setText(list.get(1));
        } else {

            System.out.println("Error: SIZE " + list.size());
            System.out.println(list.get(0) + "0");
            System.out.println(list.get(1) + "1");
            System.out.println(list.get(2) + "2");
        }
    }

}
