/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playerField;

/**
 *
 * @author Swashy
 */
public class Fireball {

    static float cursorPosX;
    static float cursorPoxY;
    static float currentPosX = -200;
    static float currentPoxY = -200;
    static double speed;
    static double angle;

    public static float getCursorPosX() {
        return cursorPosX;
    }

    public static void setCursorPosX(float cursorPosX) {
        Fireball.cursorPosX = cursorPosX;
    }

    public static float getCursorPoxY() {
        return cursorPoxY;
    }

    public static void setCursorPoxY(float cursorPoxY) {
        Fireball.cursorPoxY = cursorPoxY;
    }

    public static float getCurrentPosX() {
        return currentPosX;
    }

    public static void setCurrentPosX(float currentPosX) {
        Fireball.currentPosX = currentPosX;
    }

    public static float getCurrentPoxY() {
        return currentPoxY;
    }

    public static void setCurrentPoxY(float currentPoxY) {
        Fireball.currentPoxY = currentPoxY;
    }

    public static double getSpeed() {
        return speed;
    }

    public static void setSpeed(double speed) {
        Fireball.speed = speed;
    }

    public static double getAngle() {
        return angle;
    }

    public static void setAngle(double angle) {
        Fireball.angle = angle;
    }


}
