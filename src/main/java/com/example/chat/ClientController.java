package com.example.chat;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ClientController {

    @FXML
    private ListView listViewChatMessages;
    @FXML
    private Button btnSend;
    @FXML
    private TextArea txtAreaMessage;

    @FXML
    protected void onSendButtonClicked(ActionEvent event) {
        String message = txtAreaMessage.getText();

        listViewChatMessages.getItems().addAll(message);

    }


}