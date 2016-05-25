                          /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dataStorage.PlayersStorage;
import dataStorage.allPlayersForMasterInGame;
import dataStorage.informationStorage;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyListener;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import javafx.util.Duration;
import playerField.Fireball;
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
    private Circle c2;

    private Circle fireBallCircle;
    private Circle fireballCircle2;

    float next_point_x;
    float next_point_y;

    float next_point_x_fireball;
    float next_point_y_fireball;

    private Thread t = new Thread();
    private Thread t2 = new Thread();
    private Thread t3 = new Thread();
    private Thread t4 = new Thread();
    private Thread t5 = new Thread();

    private ArrayList xpos = new ArrayList();
    private ArrayList ypos = new ArrayList();

    private ArrayList xposFireBall = new ArrayList();
    private ArrayList yposFireBall = new ArrayList();

    private static final double SPEED = 0.75;
    private static final double knockBackSPEED = 2;
    private static final double fireBallSpeed = 2.5;

    private boolean tick = false;

    private MasterServer masterServer;
    private SlaveClient slaveClient;

    private Ticker ticker;

    private Thread bAudioThread;
    private Thread seAudioThread;

    AudioHandler audioHandler = new AudioHandler();

    private ArrayList<Shape> nodes = new ArrayList();
    private ArrayList<Shape> fireballNodes = new ArrayList<>();

    private boolean asignFireBallToMouse = false;
    private Fireball fireball = new Fireball();
    private int fireBallCooldown = 0; //5 sekunder = 640

    private boolean lockMovement = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        connectToMaster();
        player = new Player();
        Player.setPlayer(player);
        createPlayerStartPointDisplay();
        detectMovementListener();

        bAudioThread = new Thread() {
            public void run() {

                audioHandler.playback = true;
                audioHandler.defineAudioPath(audioHandler.background_audio);
            }
        };

        bAudioThread.start();

        tickRateInit();
        createItems();
        createFireBallDisplay();

        setStartingDisplayPos();

    }

    public void setStartingDisplayPos() {

        if (PlayersStorage.getPlayernumber() == 1) {
            player.setCurrentPosX(200);
            player.setCurrentPoxY(200);
        } else if (PlayersStorage.getPlayernumber() == 2) {
            player.setCurrentPosX(400);
            player.setCurrentPoxY(400);
        }
    }

    public void detectMovementListener() {
        AnchorPanePlayerField.setOnMouseClicked((MouseEvent me) -> {

            if (asignFireBallToMouse == false || fireBallCooldown > 0) {
                if (lockMovement == false) {

                    xpos.clear();
                    ypos.clear();

                    player.setCursorPosX((float) me.getX());
                    player.setCursorPoxY((float) me.getY());

                    player.setCurrentPosX((float) c1.getCenterX());
                    player.setCurrentPoxY((float) c1.getCenterY());

                    asignFireBallToMouse = false;

                    t = new Thread(() -> {
                        moveCalc();
                    });
                    t.start();
                }
            } else if (asignFireBallToMouse == true && fireBallCooldown == 0) {

                xposFireBall.clear();
                yposFireBall.clear();

                fireball.setCursorPosX((float) me.getX());
                fireball.setCursorPoxY((float) me.getY());

                fireball.setCurrentPosX((float) c1.getCenterX());
                fireball.setCurrentPoxY((float) c1.getCenterY());

                asignFireBallToMouse = false;
                fireBallCooldown = 640;

                t5 = new Thread(() -> {
                    moveCalcFireBall();
                });
                t5.start();

            }
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

    public void moveCalcFireBall() {

        xposFireBall.clear();
        yposFireBall.clear();

        int x1 = (int) fireball.getCurrentPosX(), y1 = (int) fireball.getCurrentPoxY();
        int x2 = (int) fireball.getCursorPosX(), y2 = (int) fireball.getCursorPoxY();

        double angle = Math.atan2(y2 - y1, x2 - x1);
        fireball.setAngle(angle);
        double x = x1, y = y1;

        for (int i = 0; i < 1000; i++) {
            x += fireBallSpeed * Math.cos(angle);
            y += fireBallSpeed * Math.sin(angle);
            int Xint = (int) Math.round(x);
            int Yint = (int) Math.round(y);
            next_point_x_fireball = (float) x;
            next_point_y_fireball = (float) y;
            int XNextPointInt = Math.round(next_point_x_fireball);
            int YNextPointInt = Math.round(next_point_y_fireball);
            xposFireBall.add(next_point_x_fireball);
            yposFireBall.add(next_point_y_fireball);

        }
        for (int i = 0; i < 15; i++) {

            xposFireBall.remove(0);
            yposFireBall.remove(0);
        }

        t5.interrupt();
    }

    private void playerMove() {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                if (!xpos.isEmpty() || !ypos.isEmpty()) {
                    try {
                        Double x = new Double(xpos.get(0).toString());
                        Double y = new Double(ypos.get(0).toString());
                        c1.setCenterX(x);
                        c1.setCenterY(y);
                        player.setCurrentPosX((float) xpos.get(0));
                        player.setCurrentPoxY((float) ypos.get(0));

                    } catch (Exception ex) {

                        System.out.println("SkipFrame");
                    }
                }
            }
        });

    }

    private void fireBallMove() {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                if (!xposFireBall.isEmpty() || !yposFireBall.isEmpty()) {
                    try {
                        Double x = new Double(xposFireBall.get(0).toString());
                        Double y = new Double(yposFireBall.get(0).toString());
                        fireBallCircle.setCenterX(x);
                        fireBallCircle.setCenterY(y);
                        fireball.setCurrentPosX((float) xposFireBall.get(0));
                        fireball.setCurrentPoxY((float) yposFireBall.get(0));

                    } catch (Exception ex) {

                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    public void updateEnemyDisplays() {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                if (allPlayersForMasterInGame.getNamePlayer1() != null || allPlayersForMasterInGame.getXposPlayer1() != null || allPlayersForMasterInGame.getXposPlayer1() != null || allPlayersForMasterInGame.getYposPlayer1() != null) {
                    double xForPlayer1 = Double.parseDouble(allPlayersForMasterInGame.getXposPlayer1());
                    double yForPlayer1 = Double.parseDouble(allPlayersForMasterInGame.getYposPlayer1());
                    try {
                        c2.setCenterX(xForPlayer1);
                        c2.setCenterY(yForPlayer1);
                    } catch (Exception ex) {

                        System.out.println("Skip");
                    }
                }
            }
        });
    }

    public void updateFireBallDisplay() {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                if (allPlayersForMasterInGame.getNamePlayer1FireBall() != null || allPlayersForMasterInGame.getXposPlayer1FireBall() != null || allPlayersForMasterInGame.getYposPlayer1FireBall() != null) {
                    double xForPlayer1FireBall = Double.parseDouble(allPlayersForMasterInGame.getXposPlayer1FireBall());
                    double yForPlayer1FireBall = Double.parseDouble(allPlayersForMasterInGame.getYposPlayer1FireBall());
                    try {
                        fireballCircle2.setCenterX(xForPlayer1FireBall);
                        fireballCircle2.setCenterY(yForPlayer1FireBall);
                    } catch (Exception ex) {

                        System.out.println("Skip");

                    }
                }
            }
        });
    }

    public void createFireBallDisplay() {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                fireBallCircle = new Circle(-200, -200, 10);
                fireBallCircle.setStroke(Color.BLACK);
                fireBallCircle.setFill(Color.BLACK);
                fireBallCircle.setStrokeWidth(3);
                AnchorPanePlayerField.getChildren().add(fireBallCircle);

                fireballNodes.add(fireBallCircle);
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

                        if (nodes != null) {
                            checkShapeIntersection(c1);
                        }
                        if (fireballNodes != null) {
                            checkShapeIntersectionForFireballsCollisionControll(c2);
                            checkShapeIntersectionForFireballs(c1);

                        }
                        updateEnemyDisplays();
                        updateFireBallDisplay();
                        if (fireBallCooldown > 0) {
                            fireBallCooldown = fireBallCooldown - 1;
                        }

                        if (!xpos.isEmpty() && !ypos.isEmpty()) {
                            xpos.remove(0);
                            ypos.remove(0);
                            realTimeUpdate();
                        }
                        if (!xposFireBall.isEmpty() && !yposFireBall.isEmpty()) {
                            xposFireBall.remove(0);
                            yposFireBall.remove(0);
                            
                            bAudioThread = new Thread() {
                                public void run() {

                                    System.out.println("sound effect");
                                    audioHandler.defineAudioPath(audioHandler.fireball_throw);
                                }
                            };
                            
                            fireBallMove();
                        }

                    }

                });

                while (true) {

                    ticker.update();
                    try {
                        t2.sleep(1);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(FXMLPlaygroundController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });
        t2.start();
    }

    public void createPlayerStartPointDisplay() {

        int x = 0;
        int y = 0;
        if (Player.getPlayerNumber() == 1 || Player.getPlayerNumber() == 0) { //varför blir denna 0? johan //mattias
            x = 200;
            y = 200;

        }
        if (Player.getPlayerNumber() == 2) {
            x = 400;
            y = 400;
        }
        if (Player.getPlayerNumber() == 3) {
            x = 600;
            y = 600;
        }
        if (Player.getPlayerNumber() == 4) {
            x = 800;
            y = 800;
        }

        c1 = new Circle(x, y, 20);
        PlayerStartingPoints.setC1(c1);
        c1.setStroke(Color.BLACK);
        c1.setFill(Color.BLACK);
        c1.setStrokeWidth(3);
        AnchorPanePlayerField.getChildren().add(c1);

        nodes.add(c1);
        fireballNodes.add(c1);

    }

    public void createItems() {

        System.out.println(PlayersStorage.getPlayersInLobby());
        if (PlayersStorage.getPlayersInLobby() == 2) {

            c2 = new Circle(400, 400, 20);
            PlayerStartingPoints.setC1(c2);
            c2.setStroke(Color.BLACK);
            c2.setFill(Color.BLACK);
            c2.setStrokeWidth(3);
            AnchorPanePlayerField.getChildren().add(c2);

            nodes.add(c2); // collision nodes
            fireballNodes.add(c2);

            fireballCircle2 = new Circle(400, 400, 10);
            PlayerStartingPoints.setC1(fireballCircle2);
            fireballCircle2.setStroke(Color.BLACK);
            fireballCircle2.setFill(Color.BLACK);
            fireballCircle2.setStrokeWidth(3);
            AnchorPanePlayerField.getChildren().add(fireballCircle2);

            fireballNodes.add(fireballCircle2);

        }
    }

    public void connectToMaster() {

        if (dataStorage.informationStorage.isMasterOrNot() == true) {

            masterServer = new MasterServer();
            slaveClient = new SlaveClient();

            masterServer.CreateServer(9008);
            masterServer.StartServer(9008);

            slaveClient.clientConnect(PlayersStorage.getMasterSocketIP(), 9008);
            slaveClient.tickRate(128);
            masterServer.tickRate(128);
            listenForIncommingMessagesFromMaster();
        } else {

            slaveClient = new SlaveClient();
            slaveClient.clientConnect(PlayersStorage.getMasterSocketIP(), 9008);
            slaveClient.tickRate(128);
            listenForIncommingMessagesFromMaster();
        }

    }

    @FXML
    private void exit(ActionEvent event) {
        audioHandler.stop();
        ChangeScene cs = new ChangeScene();
        cs.changeScene(event, "FXMLMainChat");
    }

    public void listenForIncommingMessagesFromMaster() {

        Task task = new Task<Void>() {
            @Override
            public Void call() {
                slaveClient.checkForIncommingMessage();
                return null;
            }
        };
        new Thread(task).start();
    }

    private void checkShapeIntersection(Shape block) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                boolean collisionDetected = false;
                for (Shape static_bloc : nodes) {
                    if (static_bloc != block) {
                        static_bloc.setFill(Color.GREEN);
                        try {
                            Shape intersect = Shape.intersect(block, static_bloc);
                            if (intersect.getBoundsInLocal().getWidth() != -1) {
                                collisionDetected = true;
                            }
                        } catch (Exception ex) {

                            System.out.println("Load");
                        }
                    }

                    if (collisionDetected) {
                        block.setFill(Color.BLUE);
                        smallKnockBack();
                    } else {
                        block.setFill(Color.GREEN);
                    }
                }
            }
        }
        );
    }

    private void checkShapeIntersectionForFireballs(Shape block) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                boolean collisionDetected = false;
                for (Shape static_bloc : fireballNodes) {
                    if (static_bloc != block) {

                        try {
                            Shape intersect = Shape.intersect(block, static_bloc);
                            if (intersect.getBoundsInLocal().getWidth() != -1) {
                                collisionDetected = true;
                            }
                        } catch (Exception ex) {

                            System.out.println("Load");
                        }
                    }
                }

                if (collisionDetected) {
                    fireballHit();
                }
            }
        });

    }

    private void checkShapeIntersectionForFireballsCollisionControll(Shape block) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                boolean collisionDetected = false;
                for (Shape static_bloc : fireballNodes) {
                    if (static_bloc != block) {

                        try {
                            Shape intersect = Shape.intersect(block, static_bloc);
                            if (intersect.getBoundsInLocal().getWidth() != -1) {
                                collisionDetected = true;
                            }
                        } catch (Exception ex) {

                            System.out.println("Load");
                        }
                    }
                }

                if (collisionDetected) {

                    Platform.runLater(new Runnable() {

                        @Override
                        public void run() {
                            fireBallCircle.setCenterX(-200);
                            fireBallCircle.setCenterY(-200);

                            fireball.setCurrentPosX(-200);
                            fireball.setCurrentPoxY(-200);

                            xposFireBall.clear();
                            yposFireBall.clear();
                        }
                    });

                }
            }
        });

    }

    public void smallKnockBack() {

        xpos.clear();
        ypos.clear();

        int x1 = (int) player.getCurrentPosX(), y1 = (int) player.getCurrentPoxY();
        int x2 = (int) player.getCursorPosX(), y2 = (int) player.getCursorPoxY();

        double angle = Math.atan2(y2 - y1, x2 - x1);
        angle = angle + 180;

        double x = x1, y = y1;

        for (int i = 0; i < 25; i++) {
            x += knockBackSPEED * Math.cos(angle);
            y += knockBackSPEED * Math.sin(angle);
            int Xint = (int) Math.round(x);
            int Yint = (int) Math.round(y);
            next_point_x = (float) x;
            next_point_y = (float) y;
            int XNextPointInt = Math.round(next_point_x);
            int YNextPointInt = Math.round(next_point_y);
            xpos.add(next_point_x);
            ypos.add(next_point_y);
            System.out.print("");
        }
    }

    public void fireballHit() {

        lockMovement = true;

        System.out.println("Hit");

        xpos.clear();
        ypos.clear();

        int x1 = (int) player.getCurrentPosX(), y1 = (int) player.getCurrentPoxY();
        int x2 = (int) player.getCursorPosX(), y2 = (int) player.getCursorPoxY();

        double angle = Double.parseDouble(allPlayersForMasterInGame.getAngleFireball());

        double x = x1, y = y1;

        for (int i = 0; i < 50; i++) {
            x += knockBackSPEED * Math.cos(angle);
            y += knockBackSPEED * Math.sin(angle);
            int Xint = (int) Math.round(x);
            int Yint = (int) Math.round(y);
            next_point_x = (float) x;
            next_point_y = (float) y;
            int XNextPointInt = Math.round(next_point_x);
            int YNextPointInt = Math.round(next_point_y);
            xpos.add(next_point_x);
            ypos.add(next_point_y);
            System.out.print("");
        }
        lockMovement = false;

        bAudioThread = new Thread() {
            public void run() {

                System.out.println("sound effect");
                audioHandler.defineAudioPath(audioHandler.fireball_hit);
            }
        };

    }

    @FXML
    private void spellListener(KeyEvent ke) {

        if (ke.getCode() == KeyCode.DIGIT1) {

            if (asignFireBallToMouse == true) {

                asignFireBallToMouse = false;
            } else if (asignFireBallToMouse == false) {

                asignFireBallToMouse = true;
            }

        }
    }

}
