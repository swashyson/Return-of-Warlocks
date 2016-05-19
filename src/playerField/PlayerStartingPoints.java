/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playerField;

import javafx.scene.shape.Circle;

/**
 *
 * @author Swashy
 */
final public class PlayerStartingPoints {

    static Circle c1;
    static PlayerStartingPoints playerStartingPoints;

    public static PlayerStartingPoints getPlayerStartingPoints() {
        return playerStartingPoints;
    }

    public static void setPlayerStartingPoints(PlayerStartingPoints playerStartingPoints) {
        PlayerStartingPoints.playerStartingPoints = playerStartingPoints;
    }

    public static Circle getC1() {
        return c1;
    }

    public static void setC1(Circle c1) {
        PlayerStartingPoints.c1 = c1;
    }

    static void setStarting() {

        c1.setCenterX(600);
        c1.setCenterY(600);
    }
    
    
}
