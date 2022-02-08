package com.example.chat;

import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class NewChatDialogController implements Initializable, PropertyChangeListener {

    @FXML
    private Button btnStartNewChat;

    @FXML
    private ScrollPane newChatPane;

    @FXML
    private ListView listViewFriends;


//    private void getFriendsList(){
//        String username = ClientController.getUsername_true();
//        System.out.println("username: " + username);

    private ObservableList<String> observableList = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        listViewFriends.getItems().add("one");

    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            listViewFriends.getItems().add(evt.getNewValue());
        });
    }
}
