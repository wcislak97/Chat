package com.example.chat;

import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ChatScreenController {

    @FXML
    private ListView listViewListOfChats;

    @FXML
    private ListView listViewChatMessages;

    @FXML
    private Button btnNewChat;

    @FXML
    private Button btnSend;

    @FXML
    private TextArea txtAreaMessage;

    @FXML
    private Label lblChatWithUsername;

    @FXML
    private void onbtnSendClicked(){
        String message=txtAreaMessage.getText();
        if(message.isBlank() || message.isBlank()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Message is empty");
            alert.showAndWait();
        }
        else{
            addMessage(message);
            txtAreaMessage.clear();
        }

    }

    private void addMessage(String message){
        listViewChatMessages.getItems().add(message);
    }




}
