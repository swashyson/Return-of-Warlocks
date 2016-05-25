/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playerField;

import dataStorage.DataStorage;
import dataStorage.PlayersStorage;
import dataStorage.allPlayersForMasterInGame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import javafx.application.Platform;
import java.util.logging.Logger;

/**
 *
 * @author Swashy
 */
public class SlaveClient {

    private static final int PORT = 9008;
    private static String SERVER = "Localhost";
    private Socket clientSocket;

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private boolean lockNewOutPutStream = false;
    private boolean lockNewinPutStream = false;

    private Thread t;
    private Thread t2;

    private Object player = Player.getPlayer();

    public void clientConnect(String server, int port) {

        try {
            //System.out.println("Attempting to connect to " + PlayersStorage.getMasterSocketIP().toString().replace("[", "").replace("]", "") + ":" + PlayersStorage.getMasterSocketPORTString().toString().replace("[", "").replace("]", ""));
            clientSocket = new Socket(PlayersStorage.getMasterSocketIP().replace("[", "").replace("]", ""), PORT);
            System.out.println("Connecion succeed" + PlayersStorage.getMasterSocketIP() + PORT);
            DataStorage.setLobbyClientSocket(clientSocket);
            System.out.println("Connecting to master while ingame");

        } catch (IOException ex) {
            System.out.println("Failed to connect to chat, is the chat server up?");
            ex.printStackTrace();
        }

    }

    public void tickRate(int time) {

        t = new Thread(new Runnable() {

            @Override
            public void run() {
                Ticker ticker = new Ticker(time); // 32 ticks per second

                ticker.addTickListener(new TickListener() {

                    @Override
                    public void onTick(float deltaTime) {
                        sendPlayerObject();
                        sendFireBallObject();

                    }
                });

                while (true) {
                    ticker.update();
                    try {
                        t.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(SlaveClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });
        t.start();
    }

    private void sendPlayerObject() {

        PrintWriter out = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("||||." + DataStorage.getUserName() + "," + Player.getCurrentPosX() + "," + Player.getCurrentPoxY() + "," + PlayersStorage.getPlayernumber() +","+Player.getPlayerHp());
            //System.out.println("send: " + DataStorage.getUserName() + "," + Player.getCurrentPosX() + "," + Player.getCurrentPoxY());
            out.flush();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    private void sendFireBallObject() {

        PrintWriter out = null;

        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            out.println("||||f" + DataStorage.getUserName() + "," + Fireball.getCurrentPosX() + "," + Fireball.getCurrentPoxY() + "," + PlayersStorage.getPlayernumber() +  "," + Fireball.getAngle());
            out.flush();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    public void checkForIncommingMessage() {
        BufferedReader in;

        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            System.out.println("RECIEVE");
            while (true) {
                String test = in.readLine();

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        String name = "";
                        if (test.contains("||||.")) {

                            name = test.substring(5);
                            String[] namesList = name.split(",");

                            String playerName = null;
                            String playerX = null;
                            String playerY = null;
                            String playerID = null;
                            String playerHP = null;
                            if (namesList.length > 3) {
                                playerName = namesList[0];
                                playerX = namesList[1];
                                playerY = namesList[2];
                                playerID = namesList[3];
                                playerHP = namesList[4];
                            } else {

                                return;
                            }

                            if (!playerName.equals(DataStorage.getUserName())) {

                                if (playerID.equals("2")) {

                                    allPlayersForMasterInGame.setNamePlayer1(playerName);
                                    allPlayersForMasterInGame.setXposPlayer1(playerX);
                                    allPlayersForMasterInGame.setYposPlayer1(playerY);
                                    allPlayersForMasterInGame.setId(playerID);
                                    allPlayersForMasterInGame.setPlayer1Hp(playerHP);
                                    //System.out.println("Name: " + namesList[0] + "X: " + namesList[1] + "Y: " + namesList[2] + "PlayerID: " + namesList[3]);

                                } else if (playerID.equals("0")) {

                                    allPlayersForMasterInGame.setNamePlayer1(playerName);
                                    allPlayersForMasterInGame.setXposPlayer1(playerX);
                                    allPlayersForMasterInGame.setYposPlayer1(playerY);
                                    allPlayersForMasterInGame.setId(playerID);
                                    allPlayersForMasterInGame.setPlayer1Hp(playerHP);
                                    
                                    //System.out.println("Name: " + namesList[0] + "X: " + namesList[1] + "Y: " + namesList[2] + "PlayerID: " + namesList[3]);
                                }

                            }

                        } if (test.contains("||||f")) {
                            

                            name = test.substring(5);
                            String[] namesList = name.split(",");

                            String playerNameFireBall = null;
                            String fireBallX = null;
                            String fireBallY = null;
                            String playerIDFireball = null;
                            String fireBallAngle = null;

                            if (namesList.length > 4) {
                                playerNameFireBall = namesList[0];
                                fireBallX = namesList[1];
                                fireBallY = namesList[2];
                                playerIDFireball = namesList[3];
                                fireBallAngle = namesList[4];
                            } else {

                                return;
                            }
                            if (!playerNameFireBall.equals(DataStorage.getUserName())) {

                                if (playerIDFireball.equals("2")) {

                                    allPlayersForMasterInGame.setNamePlayer1FireBall(playerNameFireBall);
                                    allPlayersForMasterInGame.setXposPlayer1FireBall(fireBallX);
                                    allPlayersForMasterInGame.setYposPlayer1FireBall(fireBallY);
                                    allPlayersForMasterInGame.setIdFireBall(playerIDFireball);
                                    allPlayersForMasterInGame.setAngleFireball(fireBallAngle);
                                    //System.out.println("Name: " + namesList[0] + "X: " + namesList[1] + "Y: " + namesList[2] + "PlayerID: " + namesList[3]);

                                } else if (playerIDFireball.equals("0")) {

                                    allPlayersForMasterInGame.setNamePlayer1FireBall(playerNameFireBall);
                                    allPlayersForMasterInGame.setXposPlayer1FireBall(fireBallX);
                                    allPlayersForMasterInGame.setYposPlayer1FireBall(fireBallY);
                                    allPlayersForMasterInGame.setIdFireBall(playerIDFireball);
                                    allPlayersForMasterInGame.setAngleFireball(fireBallAngle);
                                    //System.out.println("Name: " + namesList[0] + "X: " + namesList[1] + "Y: " + namesList[2] + "PlayerID: " + namesList[3]);
                                }

                            }

                        }

                    }

                });

            }
        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

}
