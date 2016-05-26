/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package playerField;

/**
 *
 * @author gul_h_000
 */
final public class Player {

    static float cursorPosX;
    static float cursorPoxY;
    static float currentPosX;
    static float currentPoxY;
    static Player player;
    static int playerNumber = dataStorage.PlayersStorage.getPlayernumber();
    static double angle;
    static int hp = Integer.parseInt(dataStorage.AllDataBaseInformation.getPlayerHP());
    static boolean playerDead = false;

    public static boolean isPlayerDead() {
        return playerDead;
    }

    public static void setPlayerDead(boolean playerDead) {
        Player.playerDead = playerDead;
    }

    public static int getHp() {
        return hp;
    }

    public static void setHp(int hp) {
        Player.hp = hp;
    }

    public static double getAngle() {
        return angle;
    }

    public static void setAngle(double angle) {
        Player.angle = angle;
    }

    public static int getPlayerNumber() {
        return playerNumber;
    }

    public static void setPlayerNumber(int playerNumber) {
        Player.playerNumber = playerNumber;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        Player.player = player;
    }

    public static float getCursorPosX() {
        return cursorPosX;
    }

    public static void setCursorPosX(float cursorPosX) {
        Player.cursorPosX = cursorPosX;
    }

    public static float getCursorPoxY() {
        return cursorPoxY;
    }

    public static void setCursorPoxY(float cursorPoxY) {
        Player.cursorPoxY = cursorPoxY;
    }

    public static float getCurrentPosX() {
        return currentPosX;
    }

    public static void setCurrentPosX(float currentPosX) {
        Player.currentPosX = currentPosX;
    }

    public static float getCurrentPoxY() {
        return currentPoxY;
    }

    public static void setCurrentPoxY(float currentPoxY) {
        Player.currentPoxY = currentPoxY;
    }

    public Player(){
    
    }
}
