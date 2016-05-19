/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatSystem;

import com.sun.jmx.snmp.BerDecoder;
import controllers.FXMLLobbyController;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        System.out.println("Running metod: clientConnect(" + server + "," + port + ")");
        try {
            clientSocket = new Socket(PlayersStorage.getMasterSocketIP().replace("[", "").replace("]", ""), PORT);

            DataStorage.setLobbyClientSocket(clientSocket);
            System.out.println("Connected to " + PlayersStorage.getMasterSocketIP().toString());
            PlayersStorage.setPlayernumber(0);
            sendMessage("||||p");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public final void saveServerInformation() {

        informationStorage.setServerIP(SERVER);
    }

    public void sendMessage(String message) {
        System.out.println("Running: sendMessage("+message+")");
        PrintWriter out = null;

        try {
            out = new PrintWriter(DataStorage.getLobbyClientSocket().getOutputStream(), true);
            out.println(message);
            out.flush();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void checkForIncommingMessage() {

        BufferedReader in = null;

        try {
            in = new BufferedReader(new InputStreamReader(dataStorage.DataStorage.getLobbyClientSocket().getInputStream()));

            while (true) {
                String test = in.readLine();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        String name = "";
                        name = test.substring(5);
                        if (test.contains("|||||")) {

                            playerNamesSplitterAndAdder();

                        } else if (test.contains("||||&")) {

                        } else if (test.contains("||||q")) {

                            updateReadyCheckDisplaysTrue(name);

                        } else if (test.contains("||||w")) {
                            updateReadyCheckDisplaysFalse(name);

                        } else if (test.contains("||||p")) {
                            System.out.println("getting "+test +"from Master");
                            PlayersStorage.setPlayersInLobby(Integer.parseInt(name));
                            int n = Integer.parseInt(name);
                            int number = n+1;
                            if (PlayersStorage.getPlayernumber() == 0) {
                                PlayersStorage.setPlayernumber(number);
                                System.out.println("Setting playernumber to " + number + ": name är "+name);
                            }
                            if (number> PlayersStorage.getPlayernumber()){
                                
                                sendAddPlayerRequest();
                            }
                            
                        } else if (test.contains("|||ap")) {
                            PlayersStorage.setPlayersInLobby(Integer.parseInt(name));
                            System.out.println("Getting message from Master:"+ test +" "+name);
                        } else {
                            DataStorage.getAllChat().appendText(test + "\n");

                        }

                    }

                    private void playerNamesSplitterAndAdder() {

                        PlayersStorage.getDisplayPlayerNamesFrames().clear();
                        String name = test.substring(5);
                        PlayersStorage.getDisplayPlayerNamesFrames().add(name.replace("[", "").replace("]", ""));
                        setPlayerFrames();
                    }
                });

            }
        } catch (SocketException ex) {
            handleSlaveDisconnect();
        } catch (Exception ex2) {

            ex2.printStackTrace();
        }

    }

    private void handleSlaveDisconnect() {
    }

    public void sendNameToServer() {

        PrintWriter out = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("|||||" + DataStorage.getUserName());
            out.flush();;
        } catch (IOException ex) {
            Logger.getLogger(Chat.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void sendAddPlayerRequest() {
        sendMessage("|||ap");

    }

    public void disconnect() {

        try {
            DataStorage.getLobbyClientSocket().close();
        } catch (Exception ex) {
            Logger.getLogger(LocalChatSlave.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPlayerFrames() {

        List<String> list = Arrays.asList(PlayersStorage.getDisplayPlayerNamesFrames().toString().split("\\s*,\\s*"));

        if (list.size() == 1) {
            PlayersStorage.getPlayer1().setText(list.get(0));
        } else if (list.size() == 2) {
            PlayersStorage.getPlayer1().setText(list.get(0));
            PlayersStorage.getPlayer2().setText(list.get(1));
        } else if (list.size() == 3) {
            PlayersStorage.getPlayer1().setText(list.get(0));
            PlayersStorage.getPlayer2().setText(list.get(1));
            PlayersStorage.getPlayer3().setText(list.get(2));
        } else if (list.size() == 4) {
            PlayersStorage.getPlayer1().setText(list.get(0));
            PlayersStorage.getPlayer2().setText(list.get(1));
            PlayersStorage.getPlayer3().setText(list.get(2));
            PlayersStorage.getPlayer4().setText(list.get(3));
        } else {

        }
        enableCheckBoxesForReadyCheck();
    }

    public void enableCheckBoxesForReadyCheck() {
        System.out.println("--------------------------");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Running: enableCheckBoxesForReadyCheck()");
                if (PlayersStorage.getPlayernumber() == 1) {
                    PlayersStorage.getReadyPlayer1().setDisable(false);
                }
                if (PlayersStorage.getPlayer2() != null) {
                    if (PlayersStorage.getPlayernumber() == 2) {
                        PlayersStorage.getReadyPlayer2().setDisable(false);
                    }
                }
                if (PlayersStorage.getPlayer3() != null) {
                    if (PlayersStorage.getPlayernumber() == 3) {
                        PlayersStorage.getReadyPlayer3().setDisable(false);
                    }
                }
                if (PlayersStorage.getPlayer4() != null) {
                    if (PlayersStorage.getPlayernumber() == 4) {
                        PlayersStorage.getReadyPlayer4().setDisable(false);
                    }
                }
            }
        });

    }

    public void updateReadyCheckDisplaysTrue(String name) {
        System.out.println("--------------------------");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Running: updateReadyCheckDisplaysTrue(" + name + ")");
                if (name.equals("1")) {
                    PlayersStorage.getReadyPlayer1().setSelected(true);
                } else if (name.equals("2")) {
                    PlayersStorage.getReadyPlayer2().setSelected(true);
                } else if (name.equals("3")) {
                    PlayersStorage.getReadyPlayer3().setSelected(true);
                } else if (name.equals("4")) {
                    PlayersStorage.getReadyPlayer4().setSelected(true);
                }
            }
        });
    }

    public void updateReadyCheckDisplaysFalse(String name) {
        System.out.println("--------------------------");
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Running: updateReadyCheckDisplaysFalse(" + name + ")");
                if (name.equals("1")) {
                    PlayersStorage.getReadyPlayer1().setSelected(false);
                } else if (name.equals("2")) {
                    PlayersStorage.getReadyPlayer2().setSelected(false);
                } else if (name.equals("3")) {
                    PlayersStorage.getReadyPlayer3().setSelected(false);
                } else if (name.equals("4")) {
                    PlayersStorage.getReadyPlayer4().setSelected(false);
                }
            }
        });
    }

    public void sendReadyCheckToMasterFirst(Boolean value) {
        System.out.println("Running: sendReadyCheckToMasterFirst(" + value + ") " + "playernumber:" + PlayersStorage.getPlayernumber());
        PrintWriter out = null;
        String newValue;
        if (value == true) {
            newValue = "||||q";
        } else {
            newValue = "||||w";
        }
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(newValue + PlayersStorage.getPlayernumber());
            System.out.println("sending:" + newValue + " : " + PlayersStorage.getPlayernumber() + " to Master");
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void sendReadyCheckToMasterSecond(Boolean value) {

        PrintWriter out = null;
        String newValue;
        if (value == true) {
            newValue = "||||q";
        } else {
            newValue = "||||w";
        }
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println(newValue + DataStorage.getUserName());
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
