/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playerField;

import dataStorage.DataStorage;
import dataStorage.PlayersStorage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
                        recievePlayerObject();

                    }
                });

                while (true) {
                    ticker.update();
                }

            }
        });
        t.start();
    }

    private void sendPlayerObject() {

        try {
            if (lockNewOutPutStream == false) {
                oos = new ObjectOutputStream(clientSocket.getOutputStream());
                lockNewOutPutStream = true;
            }
            oos.writeObject(player);
            oos.flush();

            System.out.println("SendPlayerObject");

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }

    private void recievePlayerObject() {

        try {

            if (lockNewinPutStream == false) {
                ObjectInputStream ois = new ObjectInputStream(clientSocket.getInputStream());
                lockNewinPutStream = true;
            }
            
            

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
}
