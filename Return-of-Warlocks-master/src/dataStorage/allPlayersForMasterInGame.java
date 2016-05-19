/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataStorage;

import java.util.ArrayList;
import playerField.Player;

/**
 *
 * @author Swashy
 */
public class allPlayersForMasterInGame {
    
    static ArrayList<Player> playerObjects = new ArrayList();

    public static ArrayList<Player> getPlayerObjects() {
        return playerObjects;
    }

    public static void setPlayerObjects(ArrayList<Player> playerObjects) {
        allPlayersForMasterInGame.playerObjects = playerObjects;
    }

    
}
