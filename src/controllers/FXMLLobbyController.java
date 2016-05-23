/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import chatSystem.Chat;
import chatSystem.AllChatToLocal;
import chatSystem.LocalChatMaster;
import chatSystem.LocalChatSlave;
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
import javafx.scene.control.CheckBox;
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
public class FXMLLobbyController implements Initializable {

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
    @FXML
    private CheckBox Player1Ready;

    public CheckBox getPlayer1Ready() {
        return Player1Ready;
    }

    public CheckBox getPlayer2Ready() {
        return Player2Ready;
    }
    @FXML
    private CheckBox Player2Ready;

    @FXML
    private Label player1;
    @FXML
    private Label player2;
    @FXML
    private Label player3;
    @FXML
    private Label player4;

    @FXML
    private CheckBox readyPlayer1;
    @FXML
    private CheckBox readyPlayer2;
    @FXML
    private CheckBox readyPlayer3;
    @FXML
    private CheckBox readyPlayer4;

    chatSystem.AllChatToLocal chat;
    chatSystem.LocalChatMaster masterChat;
    chatSystem.LocalChatSlave slaveChat;

    //ChangeScene cs = new ChangeScene();
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText(typeToChat.getText());
        slaveChat.sendMessage(typeToChat.getText());
        typeToChat.clear();
    }

    @FXML
    private void handleServerList(ActionEvent event) {

        lobbyList.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void handleDisconnectFromLobby(ActionEvent event) {

        ChangeScene cs = new ChangeScene();
        if (informationStorage.isMasterOrNot() == true) {

            masterChat.Disconnect();
            chatMasterSlaveNul();
            cs.changeScene(event, "FXMLMainChat");
            //changeScene(event);
        } else {
            slaveChat.disconnect();
            chatMasterSlaveNul();
            cs.changeScene(event, "FXMLMainChat");
            //changeScene(event);
        }

    }

    @FXML
    private void handleReadyCheck(ActionEvent event) {
    }

    // ska öppna spelplanen härifrån?!?!?!?!? 
    // funkar inte för tillfället
    @FXML
    private void startGameHandler(ActionEvent event) {

        if (informationStorage.isMasterOrNot() == true) {

            masterChat.Disconnect();
            slaveChat.disconnect();
        } else {

            slaveChat.disconnect();
        }
        ChangeScene cs = new ChangeScene();
        cs.changeScene(event, "FXMLPlayground");

    }

    private void chatMasterSlaveNul() {
        chat = null;
        masterChat = null;
        slaveChat = null;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        chat = new AllChatToLocal();
        slaveChat = new LocalChatSlave();
        if (informationStorage.isMasterOrNot() == true) {
            masterChat = new LocalChatMaster();
            masterChat.CreateServer(9007);
            masterChat.StartServer(9007);
            masterChat.broadCastLobbys();
            masterChat.sendMasterPort();
            masterChat.sendMasterIP();
        }

        listenForIncommingMessagesFromServer(chat);
        keyListener(chat, slaveChat);
        startAllChat();
        startPlayerFrames();
        startReadyCheckFrames();

        slaveChat.clientConnect(PlayersStorage.getMasterSocketIP(), PlayersStorage.getMasterSocketPORT());
        listenForIncommingMessagesFromMaster(slaveChat);
        slaveChat.sendNameToServer();
        readyCheckLsitener1();
        readyCheckLsitener2();
        readyCheckLsitener3();
        readyCheckLsitener4();

    }

    public void startAllChat() {

        DataStorage.setAllChat(allChat);
        welcomeMessage();

    }

    public void startPlayerFrames() {

        PlayersStorage.setPlayer1(player1);
        PlayersStorage.setPlayer2(player2);

    }

    public void startReadyCheckFrames() {

        PlayersStorage.setReadyPlayer1(readyPlayer1);
        PlayersStorage.setReadyPlayer2(readyPlayer2);

    }

    public void welcomeMessage() {

        DataStorage.getAllChat().appendText("Welcome to the chat server at ip: " + informationStorage.getServerIP() + "\n");
        DataStorage.getAllChat().appendText("Please use /all to type in all chat\n");
        DataStorage.getAllChat().appendText("Please keep the chat mature \n");
    }

    private void readyCheckLsitener1() {

        readyPlayer1.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {

            if (PlayersStorage.getPlayernumber() == 1) {

                slaveChat.sendReadyCheckToMasterFirst(newValue);
                System.out.println("playernumber = 1 is ready: " + readyPlayer1.isSelected());
                System.out.println("playernumber = 2 is ready: " + readyPlayer2.isSelected());
                System.out.println("playernumber = 3 is ready: " + readyPlayer3.isSelected());
                System.out.println("playernumber = 4 is ready: " + readyPlayer4.isSelected());
            }
        });
    }

    private void readyCheckLsitener2() {

        readyPlayer2.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (PlayersStorage.getPlayernumber() == 2) {

                slaveChat.sendReadyCheckToMasterFirst(newValue);
                System.out.println("playernumber = 1 is ready: " + readyPlayer1.isSelected());
                System.out.println("playernumber = 2 is ready: " + readyPlayer2.isSelected());
                System.out.println("playernumber = 3 is ready: " + readyPlayer3.isSelected());
                System.out.println("playernumber = 4 is ready: " + readyPlayer4.isSelected());
            }
        });
    }

    private void readyCheckLsitener3() {

        readyPlayer3.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (PlayersStorage.getPlayernumber() == 3) {

                slaveChat.sendReadyCheckToMasterFirst(newValue);
                System.out.println("playernumber = 1 is ready: " + readyPlayer1.isSelected());
                System.out.println("playernumber = 2 is ready: " + readyPlayer2.isSelected());
                System.out.println("playernumber = 3 is ready: " + readyPlayer3.isSelected());
                System.out.println("playernumber = 4 is ready: " + readyPlayer4.isSelected());
            }
        });
    }

    private void readyCheckLsitener4() {

        readyPlayer4.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (PlayersStorage.getPlayernumber() == 4) {
                System.out.println("playernumber = 1 is ready: " + readyPlayer1.isSelected());
                System.out.println("playernumber = 2 is ready: " + readyPlayer2.isSelected());
                System.out.println("playernumber = 3 is ready: " + readyPlayer3.isSelected());
                System.out.println("playernumber = 4 is ready: " + readyPlayer4.isSelected());
                slaveChat.sendReadyCheckToMasterFirst(newValue);
            }
        });
    }

    private void keyListener(chatSystem.AllChatToLocal chat, chatSystem.LocalChatSlave chat2) {
        typeToChat.setOnKeyPressed((KeyEvent ke) -> {
            if (ke.getCode().equals(KeyCode.ENTER)) {
                String upToNCharacters = typeToChat.getText().substring(0, Math.min(typeToChat.getText().length(), 5));

                System.out.println("SUB" + upToNCharacters);
                if (upToNCharacters.contains("/all")) {

                    chat.sendMessage(typeToChat.getText().substring(5));
                    typeToChat.clear();
                    System.out.println("Mainchat");

                } else {

                    chat2.sendMessage(typeToChat.getText());
                    typeToChat.clear();
                    System.out.println("LobbyChat");
                }

            }
        });
    }

    public void listenForIncommingMessagesFromServer(chatSystem.AllChatToLocal chat) {

        Task task = new Task<Void>() {
            @Override
            public Void call() {
                chat.checkForIncommingMessage();
                return null;
            }
        };
        new Thread(task).start();
    }

    public void listenForIncommingMessagesFromMaster(chatSystem.LocalChatSlave chat) {

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
