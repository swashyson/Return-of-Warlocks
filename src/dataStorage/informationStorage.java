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
final public class informationStorage {
    
    static String serverIP;
    static boolean masterOrNot;
    static int dontRetry = 0;

    public static int getDontRetry() {
        return dontRetry;
    }

    public static void setDontRetry(int dontRetry) {
        informationStorage.dontRetry = dontRetry;
    }

    public static boolean isMasterOrNot() {
        return masterOrNot;
    }

    public static void setMasterOrNot(boolean masterOrNot) {
        informationStorage.masterOrNot = masterOrNot;
    }

    public static String getServerIP() {
        return serverIP;
    }

    public static void setServerIP(String serverIP) {
        informationStorage.serverIP = serverIP;
    }
    
    public informationStorage(){
    
    }
    
}
