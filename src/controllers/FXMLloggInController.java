/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import dataStorage.DataStorage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Connections.ConnectToServer;
/**
 *
 * @author Swashy
 */
public class FXMLloggInController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Button startButton;
    @FXML
    private TextField textfieldName;
    
    private int isConnectedToServer = 0;
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        
        ConnectToServer cTS = new ConnectToServer();

        if (checkName() == true) {

            storeName();
            cTS.connectToServer("Localhost",9006);
            System.out.println("checking is connected: "+ isConnectedToServer);
            if(DataStorage.getChatClientSocket() != null){
                System.out.println("changeing scene");
                changeScene(event);
            }else{
                label.setText("Couldent connect to server");
            }
            System.out.println("connected " + isConnectedToServer);
            

        } else {
            label.setText("Skriv in ett giltigt namn");
            System.out.println("Skriv in ett giltigt namn");
        }
    }

    public void changeScene(ActionEvent event) {
        
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GameLayouts/FXMLMainChat.fxml"));
            Scene scene = new Scene(root);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void setisConnectedToServer(int b){
        System.out.println("setting isConnectedToServer to " + b);
        isConnectedToServer = b;
    }
            

    public boolean checkName() {

        return !textfieldName.getText().isEmpty();
    }
    public void storeName(){
    
        DataStorage.setUserName(textfieldName.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        System.out.println(com.sun.javafx.runtime.VersionInfo.getRuntimeVersion());
    }

}
