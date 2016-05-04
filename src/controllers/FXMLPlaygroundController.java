/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Mohini
 */
public class FXMLPlaygroundController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    private void temporaryBack(ActionEvent event){
        
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
    
}
