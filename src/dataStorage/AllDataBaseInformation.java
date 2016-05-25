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
public class AllDataBaseInformation {

    static String playerSpeed;
    static String playerHP;
    static String playerKnockBackSpeed;

    static String fireBallSpeed;
    static String fireBallDamage;
    static String fireBallCD;

    static String lavaDamage;
    static boolean isConnected = false;

    public static String getFireBallCD() {
        return fireBallCD;
    }

    public static void setFireBallCD(String fireBallCD) {
        AllDataBaseInformation.fireBallCD = fireBallCD;
    }

    public static boolean isIsConnected() {
        return isConnected;
    }

    public static void setIsConnected(boolean isConnected) {
        AllDataBaseInformation.isConnected = isConnected;
    }

    public static String getPlayerSpeed() {
        return playerSpeed;
    }

    public static void setPlayerSpeed(String playerSpeed) {
        AllDataBaseInformation.playerSpeed = playerSpeed;
    }

    public static String getPlayerHP() {
        return playerHP;
    }

    public static void setPlayerHP(String playerHP) {
        AllDataBaseInformation.playerHP = playerHP;
    }

    public static String getPlayerKnockBackSpeed() {
        return playerKnockBackSpeed;
    }

    public static void setPlayerKnockBackSpeed(String playerKnockBackSpeed) {
        AllDataBaseInformation.playerKnockBackSpeed = playerKnockBackSpeed;
    }

    public static String getFireBallSpeed() {
        return fireBallSpeed;
    }

    public static void setFireBallSpeed(String fireBallSpeed) {
        AllDataBaseInformation.fireBallSpeed = fireBallSpeed;
    }

    public static String getFireBallDamage() {
        return fireBallDamage;
    }

    public static void setFireBallDamage(String fireBallDamage) {
        AllDataBaseInformation.fireBallDamage = fireBallDamage;
    }

    public static String getLavaDamage() {
        return lavaDamage;
    }

    public static void setLavaDamage(String lavaDamage) {
        AllDataBaseInformation.lavaDamage = lavaDamage;
    }

}
