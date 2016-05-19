/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dataStorage.PlayersStorage;
import playerField.Player;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import playerField.MasterServer;
import playerField.PlayerStartingPoints;
import playerField.SlaveClient;
import playerField.TickListener;
import playerField.Ticker;

/**
 * FXML Controller class
 *
 * @author Mohini
 */
public class FXMLPlaygroundController implements Initializable {

    @FXML
    private AnchorPane AnchorPanePlayerField;

    private playerField.Player player;

    private Circle c1;

    float next_point_x;
    float next_point_y;

    private Thread t = new Thread();
    private Thread t2 = new Thread();
    private Thread t3 = new Thread();
    private Thread t4 = new Thread();

    private ArrayList xpos = new ArrayList();
    private ArrayList ypos = new ArrayList();

    private static final double SPEED = 0.75;

    private boolean tick = false;

    private MasterServer masterServer;
    private SlaveClient slaveClient;

    private Ticker ticker;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        connectToMaster();
        player = new Player();
        Player.setPlayer(player);
        createPlayerStartPointDisplay();
        detectMovementListener();
        tickRateInit();

    }

    public void detectMovementListener() {
        AnchorPanePlayerField.setOnMouseClicked((MouseEvent me) -> {

            player.setCursorPosX((float) me.getSceneX());
            player.setCursorPoxY((float) me.getSceneY());

            player.setCurrentPosX((float) c1.getCenterX());
            player.setCurrentPoxY((float) c1.getCenterY());

            //System.out.println("Du klickade på X: " + playerMovement.getCursorPosX() + "Y: " + playerMovement.getCursorPoxY());
            t = new Thread(() -> {
                moveCalc();
            });
            t.start();
        });

    }

    public void realTimeUpdate() {

        playerMove();
    }

    public void moveCalc() {

        xpos.clear();
        ypos.clear();

        int x1 = (int) player.getCurrentPosX(), y1 = (int) player.getCurrentPoxY();
        int x2 = (int) player.getCursorPosX(), y2 = (int) player.getCursorPoxY();

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double x = x1, y = y1;

        while ((x != x2 && y != y2)) {
            x += SPEED * Math.cos(angle);
            y += SPEED * Math.sin(angle);
            int Xint = (int) Math.round(x);
            int Yint = (int) Math.round(y);
            next_point_x = (float) x;
            next_point_y = (float) y;
            int XNextPointInt = Math.round(next_point_x);
            int YNextPointInt = Math.round(next_point_y);
            xpos.add(next_point_x);
            ypos.add(next_point_y);
            System.out.print("");

            if (XNextPointInt == x2) {

                System.out.println("test");
                break;
            }

        }
        t.interrupt();
    }

    private void playerMove() {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                if (!xpos.isEmpty() || !ypos.isEmpty()) {
                    Double x = new Double(xpos.get(0).toString());
                    Double y = new Double(ypos.get(0).toString());
                    c1.setCenterX(x);
                    c1.setCenterY(y);
                    player.setCurrentPosX((float) xpos.get(0));
                    player.setCurrentPoxY((float) ypos.get(0));

                }
            }
        });

    }

    public void tickRateInit() {

        t2 = new Thread(new Runnable() {

            @Override
            public void run() {
                ticker = new Ticker(128); // 32 ticks per second

                ticker.addTickListener(new TickListener() {

                    @Override
                    public void onTick(float deltaTime) {
                        //System.out.println(String.format("Ticked with deltaTime %f", deltaTime));
                        //realTimeUpdate();
                        if (!xpos.isEmpty() || !ypos.isEmpty()) {
                            xpos.remove(0);
                            ypos.remove(0);
                            realTimeUpdate();
                        }

                    }

                });

                while (true) {

                    ticker.update();
                }

            }
        });
        t2.start();
    }

    public void createPlayerStartPointDisplay() {

        int x = 0;
        int y = 0;
        if (Player.getPlayerNumber() == 1) {
            x = 200;
            y = 200;
        } else if (Player.getPlayerNumber() == 2) {
            x = 400;
            y = 400;
        } else if (Player.getPlayerNumber() == 3) {
            x = 600;
            y = 600;
        } else {
            x = 800;
            y = 800;
        }

        c1 = new Circle(x, y, 20);
        PlayerStartingPoints.setC1(c1);
        c1.setStroke(Color.BLACK);
        c1.setFill(Color.BLACK);
        c1.setStrokeWidth(3);
        AnchorPanePlayerField.getChildren().add(c1);
    }

    public void connectToMaster() {

        if (dataStorage.informationStorage.isMasterOrNot() == true) {

            masterServer = new MasterServer();
            slaveClient = new SlaveClient();

            masterServer.CreateServer(9008);
            masterServer.StartServer(9008);

            slaveClient.clientConnect(PlayersStorage.getMasterSocketIP(), 9008);
            slaveClient.tickRate(2);
            masterServer.tickRate(2);
        } else {

            slaveClient = new SlaveClient();
            slaveClient.clientConnect(PlayersStorage.getMasterSocketIP(), 9008);
            slaveClient.tickRate(2);
        }

    }

  
}
