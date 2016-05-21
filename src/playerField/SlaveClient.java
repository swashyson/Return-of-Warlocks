/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playerField;

import dataStorage.DataStorage;
import dataStorage.PlayersStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
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
                        sendPlayerPosToMaster();
                        //recievePlayerObject();

                    }
                });

                while (true) {
                    ticker.update();
                }

            }
        });
        t.start();
    }
    public void sendMessageToMaster(String message){
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(DataStorage.getLobbyClientSocket().getOutputStream());
            pw.write(message);
            pw.flush();
        } catch (Exception e) {
        }
    }
    private void sendPlayerPosToMaster() {
        sendMessageToMaster("||pos"+Player.currentPosX+Player.currentPoxY+Player.playerNumber);
    }

    private void recievePlayerPos() {
        BufferedReader in = null;
            String readMessage;
            String readdata;
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                readMessage = in.readLine();
                readdata = readMessage.substring(5);
                if(readMessage.contains("||pos")){
                    
                    
                    
                }
             
            } catch (Exception ex) {

                ex.printStackTrace();
            }
    }
}
