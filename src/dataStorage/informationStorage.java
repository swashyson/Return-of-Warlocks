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

    public static String getServerIP() {
        return serverIP;
    }

    public static void setServerIP(String serverIP) {
        informationStorage.serverIP = serverIP;
    }
    
    public informationStorage(){
    
    }
    
}
