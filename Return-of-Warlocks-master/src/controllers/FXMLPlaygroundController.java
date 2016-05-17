/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import PlayerGround.PlayerMovement;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
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

/**
 * FXML Controller class
 *
 * @author Mohini
 */
public class FXMLPlaygroundController implements Initializable {

    @FXML
    private AnchorPane AnchorPanePlayerField;

    private PlayerGround.PlayerMovement playerMovement;

    private Circle c1;

    float next_point_x;
    float next_point_y;


    Thread t;
    
    private static final double SPEED = 0.25;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //här ska mastern egentligen skapa en start pos till en spelare men detta är bara en movement test
        playerMovement = new PlayerMovement();
        createPlayerStartPointDisplay();
        detectMovementListener();
        realTimeUpdate();
    }

    public void detectMovementListener() {
        AnchorPanePlayerField.setOnMouseClicked((MouseEvent me) -> {
            playerMovement.setCursorPosX((float) me.getSceneX());
            playerMovement.setCursorPoxY((float) me.getSceneY());

            playerMovement.setCurrentPosX((float) c1.getCenterX());
            playerMovement.setCurrentPoxY((float) c1.getCenterY());
            //System.out.println("Du klickade på X: " + playerMovement.getCursorPosX() + "Y: " + playerMovement.getCursorPoxY());

            t = new Thread(new Runnable() {

                @Override
                public void run() {
                    moveCalc();
                }
            });
            t.start();
        });
    }

    public void realTimeUpdate() {

        Platform.runLater(() -> {
            playerMove();
        });

    }

    public void moveCalc() {

        int x1 = (int) playerMovement.getCurrentPosX(), y1 = (int) playerMovement.getCurrentPoxY();
        int x2 = (int) playerMovement.getCursorPosX(), y2 = (int) playerMovement.getCursorPoxY();

        double angle = Math.atan2(y2 - y1, x2 - x1);
        double x = x1, y = y1;

        while (x < x2 && y < y2 ) {
            x += SPEED * Math.cos(angle);
            y += SPEED * Math.sin(angle);
            System.out.println(x + ", " + y);
            next_point_x = (float) x;
            next_point_y = (float) y;
            realTimeUpdate();
        }
        
        System.out.println("Done");

    }

    private void playerMove() {

        c1.setCenterX(next_point_x);
        c1.setCenterY(next_point_y);

    }

    public void createPlayerStartPointDisplay() {

        c1 = new Circle(422, 350, 20);
        c1.setStroke(Color.BLACK);
        c1.setFill(Color.BLACK);
        c1.setStrokeWidth(3);
        AnchorPanePlayerField.getChildren().add(c1);
    }

    private void temporaryBack(ActionEvent event) {

        try {
            Parent blah = FXMLLoader.load(getClass().getResource("/GameLayouts/FXMLMainChat.fxml"));
            Scene scene = new Scene(blah);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
