/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Connections;

import dataStorage.AllDataBaseInformation;
import dataStorage.DataStorage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Swashy
 */
public class dataBaseConnect {

    String url = "jdbc:mysql://85.197.159.180:3306/tomra"; // ändra beroende på vilket dator ni sitter på
    String user = "mohini";
    String password = "mohini";

    Connection con = null;
    Statement st = null;
    ResultSet rs = null;

    PreparedStatement pst = null;

    public void connect() {

        try {
            con = DriverManager.getConnection(url, user, password);
            st = con.createStatement();
            rs = st.executeQuery("SELECT VERSION()");
        } catch (SQLException ex) {
            //ex.printStackTrace();
            DataStorage.getAllChat().appendText("Failed to connect to the Database please reconnect");
        }
    }

    public void loadAllValuesIntoGame() {

        try {
            pst = con.prepareStatement("SELECT * FROM Player,Fireball,Lava;");
            rs = pst.executeQuery();

            while (rs.next()) {
                AllDataBaseInformation.setPlayerSpeed(rs.getString("PlayerSpeed"));
                AllDataBaseInformation.setPlayerHP(rs.getString("PlayerHP"));
                AllDataBaseInformation.setPlayerKnockBackSpeed(rs.getString("PlayerKnockBackSpeed"));
                AllDataBaseInformation.setFireBallDamage(rs.getString("FireballDamage"));
                AllDataBaseInformation.setFireBallSpeed(rs.getString("FireballSpeed"));
                AllDataBaseInformation.setFireBallCD(rs.getString("FireballCD"));
                AllDataBaseInformation.setLavaDamage(rs.getString("LavaDamage"));

                AllDataBaseInformation.setIsConnected(true);
            }
        } catch (Exception ex) {

            ex.printStackTrace();
        } finally {

            try {
                con.close();
                st.close();
                rs.close();
                pst.close();

            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
