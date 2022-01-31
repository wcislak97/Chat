package com.example.chat;

import javafx.application.Platform;
import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatScreenController implements PropertyChangeListener {

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

    public ChatScreenController() {
        Client.clientConnection.addListener(this);
    }

    @FXML
    private void onbtnSendClicked(){
        String message=txtAreaMessage.getText();
        if (message.isBlank() || message.isBlank()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Message is empty");
            alert.showAndWait();
        } else {
            addMessage(message);
            txtAreaMessage.clear();
        }
    }

    private void addMessage(String message){
        if(checkIfBadWord(message)) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Napisano brzydkie słowo!!!");
            alert.showAndWait();
        } else {
            listViewChatMessages.getItems().add(message);
            try {
                Client.clientConnection.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkIfBadWord(String message){
        List<String> bad_words_list = new ArrayList<>();
        bad_words_list.add("dupa");

        try{
            DatabaseConnection db = new DatabaseConnection();
            ResultSet rs;
            rs=db.selectStatement("SELECT bad_word FROM bad_words");

            while(rs.next()){
                bad_words_list.add(rs.getString("bad_word"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        for(String word : bad_words_list){
            if(message.contains(word)){
                return true;
            }
        }
        return false;
    }


    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            listViewChatMessages.getItems().add(evt.getNewValue());
        });
    }
}
