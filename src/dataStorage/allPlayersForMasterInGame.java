/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStorage;

/**
 *
 * @author Swashy
 */
public class allPlayersForMasterInGame {

    static String namePlayer1;
    static String XposPlayer1;
    static String YposPlayer1;
    static String Player1Hp; 
    static String id;
    static String playerAngle;
    static String degress;

    static String hpplayer1;

    static String idFireBall;
    static String namePlayer1FireBall;
    static String XposPlayer1FireBall;
    static String YposPlayer1FireBall;
    static String angleFireball;

    static String namePlayer2; // 3 spelare
    static String XposPlayer2;
    static String YposPlayer2;
    static String Player2HP;

    public static String getPlayer1Hp() {
        return Player1Hp;
    }

    public static void setPlayer1Hp(String Player1Hp) {
        allPlayersForMasterInGame.Player1Hp = Player1Hp;
    }

    public static String getPlayer2HP() {
        return Player2HP;
    }

    public static void setPlayer2HP(String Player2HP) {
        allPlayersForMasterInGame.Player2HP = Player2HP;
    }
    static String id2;

    static int hp = Integer.parseInt(dataStorage.AllDataBaseInformation.getPlayerHP());
    static boolean playerDead = false;

    public static String getDegress() {
        return degress;
    }

    public static void setDegress(String degress) {
        allPlayersForMasterInGame.degress = degress;
    }

    public static int getHp() {
        return hp;
    }

    public static void setHp(int hp) {
        allPlayersForMasterInGame.hp = hp;
    }

    public static boolean isPlayerDead() {
        return playerDead;
    }

    public static void setPlayerDead(boolean playerDead) {
        allPlayersForMasterInGame.playerDead = playerDead;
    }

    public static String getHpplayer1() {
        return hpplayer1;
    }

    public static void setHpplayer1(String hpplayer1) {
        allPlayersForMasterInGame.hpplayer1 = hpplayer1;
    }

    public static String getPlayerAngle() {
        return playerAngle;
    }

    public static void setPlayerAngle(String playerAngle) {
        allPlayersForMasterInGame.playerAngle = playerAngle;
    }

    public static String getAngleFireball() {
        return angleFireball;
    }

    public static void setAngleFireball(String angleFireball) {
        allPlayersForMasterInGame.angleFireball = angleFireball;
    }

    public static String getNamePlayer1FireBall() {
        return namePlayer1FireBall;
    }

    public static void setNamePlayer1FireBall(String namePlayer1FireBall) {
        allPlayersForMasterInGame.namePlayer1FireBall = namePlayer1FireBall;
    }

    public static String getXposPlayer1FireBall() {
        return XposPlayer1FireBall;
    }

    public static void setXposPlayer1FireBall(String XposPlayer1FireBall) {
        allPlayersForMasterInGame.XposPlayer1FireBall = XposPlayer1FireBall;
    }

    public static String getYposPlayer1FireBall() {
        return YposPlayer1FireBall;
    }

    public static void setYposPlayer1FireBall(String YposPlayer1FireBall) {
        allPlayersForMasterInGame.YposPlayer1FireBall = YposPlayer1FireBall;
    }

    public static String getIdFireBall() {
        return idFireBall;
    }

    public static void setIdFireBall(String idFireBall) {
        allPlayersForMasterInGame.idFireBall = idFireBall;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        allPlayersForMasterInGame.id = id;
    }

    public static String getNamePlayer2() {
        return namePlayer2;
    }

    public static void setNamePlayer2(String namePlayer2) {
        allPlayersForMasterInGame.namePlayer2 = namePlayer2;
    }

    public static String getXposPlayer2() {
        return XposPlayer2;
    }

    public static void setXposPlayer2(String XposPlayer2) {
        allPlayersForMasterInGame.XposPlayer2 = XposPlayer2;
    }

    public static String getYposPlayer2() {
        return YposPlayer2;
    }

    public static void setYposPlayer2(String YposPlayer2) {
        allPlayersForMasterInGame.YposPlayer2 = YposPlayer2;
    }

    public static String getId2() {
        return id2;
    }

    public static void setId2(String id2) {
        allPlayersForMasterInGame.id2 = id2;
    }

    public static String getNamePlayer1() {
        return namePlayer1;
    }

    public static void setNamePlayer1(String namePlayer1) {
        allPlayersForMasterInGame.namePlayer1 = namePlayer1;
    }

    public static String getXposPlayer1() {
        return XposPlayer1;
    }

    public static void setXposPlayer1(String XposPlayer1) {
        allPlayersForMasterInGame.XposPlayer1 = XposPlayer1;
    }

    public static String getYposPlayer1() {
        return YposPlayer1;
    }

    public static void setYposPlayer1(String YposPlayer1) {
        allPlayersForMasterInGame.YposPlayer1 = YposPlayer1;
    }

}
