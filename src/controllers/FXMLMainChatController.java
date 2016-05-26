/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import Connections.dataBaseConnect;
import chatSystem.Chat;
import dataStorage.AllDataBaseInformation;
import dataStorage.DataStorage;
import dataStorage.PlayersStorage;
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
    int x = 0;

    ChangeScene cs = new ChangeScene();

    AudioHandler ah = new AudioHandler();

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
        handleDataBase();

        if (AllDataBaseInformation.isIsConnected()) {
            informationStorage.setMasterOrNot(true);
            cs.changeScene(event, "FXMLLobby");
        } else {

            errorMessageInChat();
        }
    }

    @FXML
    private void joinGame(ActionEvent event) {
        handleDataBase();

        if (AllDataBaseInformation.isIsConnected()) {
            informationStorage.setMasterOrNot(false);
            cs.changeScene(event, "FXMLLobby");
        } else {

            errorMessageInChat();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        if (informationStorage.getDontRetry() == 0) {
            chat.sendNameToServer();
        }
        listenForIncommingMessages(chat);
        chat.requestNameList();
        chat.requestLobbyList();
        chat.requestIPList();
        chat.requestPORTList();
        keyListener(chat);
        startAllChat();
        startPlayerList();
        startLobbyList();
        lobbyListListener();
        informationStorage.setDontRetry(1);

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

    public void errorMessageInChat() {

        DataStorage.getAllChat().appendText("Failed to connect to the Database please reconnect");
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

    public void handleDataBase() {

        dataBaseConnect connect = new dataBaseConnect();
        connect.connect();
        connect.loadAllValuesIntoGame();

    }

    @FXML
    private void exit(ActionEvent event) {
//        cs.changeScene(event, "FXMLloggIn");
        System.exit(0);
    }
}
