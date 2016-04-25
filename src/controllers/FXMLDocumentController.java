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

/**
 *
 * @author Swashy
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Button startButton;
    @FXML
    private TextField textfieldName;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");

        if (checkName() == true) {

            storeName();
            changeScene(event);

        } else {

            System.out.println("Skriv in ett giltigt namn");
        }
    }

    private void changeScene(ActionEvent event) {
        
        try {
            Parent blah = FXMLLoader.load(getClass().getResource("/GameLayouts/FXMLDocumentSecondScene.fxml"));
            Scene scene = new Scene(blah);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
