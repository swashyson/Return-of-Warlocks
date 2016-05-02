/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import chatSystem.Chat;
import dataStorage.DataStorage;
import dataStorage.informationStorage;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author Swashy
 */
public class FXMLDocumentSecondScene implements Initializable {

    @FXML
    private ListView playerList;
    @FXML
    private Label label;
    @FXML
    private Button sendButton;
    @FXML
    private TextField typeToChat;
    @FXML
    private TextArea allChat;

    chatSystem.Chat chat = new Chat();

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText(typeToChat.getText());
        chat.sendMessage(typeToChat.getText());
        typeToChat.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        
        chat.clientConnect(chat.getServerIp(), 9006);
        listenForIncommingMessages(chat);
        keyListener(chat);
        startAllChat();
        startPlayerList();
        chat.sendNameToServer();
    }

    public void startAllChat() {

        DataStorage.setAllChat(allChat);
        welcomeMessage();

    }
    public void startPlayerList(){
    
        DataStorage.setPlayerList(playerList);
    }

    public void welcomeMessage() {

        DataStorage.getAllChat().appendText("Welcome to the chat server at ip: " + informationStorage.getServerIP() + "\n");
        DataStorage.getAllChat().appendText("Please keep the chat mature \n");
    }

    private void keyListener(chatSystem.Chat chat) {
        typeToChat.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                chat.sendMessage(typeToChat.getText());
                typeToChat.clear();
                System.out.println("send with TCP");
            }
        });
    }

    public void listenForIncommingMessages(chatSystem.Chat chat) {

        Task task = new Task<Void>() {
            @Override
            public Void call() {
                chat.checkForIncommingMessage();
                return null;
            }
        };
        new Thread(task).start();
    }

}
