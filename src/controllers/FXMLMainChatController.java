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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
public class FXMLMainChatController implements Initializable {

    @FXML
    private ListView playerList;
    @FXML
    private ListView lobbyList;
    @FXML
    private Label label;
    @FXML
    private Button sendButton;
    @FXML
    private TextField typeToChat;
    @FXML
    private TextArea allChat;
    @FXML
    private Button createGameButton;

    chatSystem.Chat chat = new Chat();
    int x  = 0;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText(typeToChat.getText());
        chat.sendMessage(typeToChat.getText());
        typeToChat.clear();
    }

    public void lobbyListListener() {

        lobbyList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    int id = lobbyList.getSelectionModel().getSelectedIndex();
                    chat.selectedServerJoin(id);
                }
        );
    }

    @FXML
    private void createGame(ActionEvent event) {

        changeScene(event, true);
    }

    @FXML
    private void joinGame(ActionEvent event) {

        changeScene(event, false);
    }

    private void changeScene(ActionEvent event, boolean masterOrNot) {

        try {
            informationStorage.setMasterOrNot(masterOrNot);

            Parent blah = FXMLLoader.load(getClass().getResource("/GameLayouts/FXMLLobby.fxml"));
            Scene scene = new Scene(blah);
            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(scene);
            appStage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //DISC
        
        
        chat.sendNameToServer();
        listenForIncommingMessages(chat);
        System.out.println("true hej ");
        chat.requestNameList();
        chat.requestLobbyList();
        keyListener(chat);
        startAllChat();
        startPlayerList();
        startLobbyList();
        lobbyListListener();
    }

    public void startAllChat() {

        DataStorage.setAllChat(allChat);
        welcomeMessage();

    }

    public void startPlayerList() {

        DataStorage.setPlayerList(playerList);
    }

    public void startLobbyList() {

        DataStorage.setLobbyList(lobbyList);
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
