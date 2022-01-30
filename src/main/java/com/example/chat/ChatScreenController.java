package com.example.chat;

import javafx.beans.binding.StringBinding;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.SQLException;
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
        if(checkIfBadWord(message))
        {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Napisano brzydkie słowo!!!");
            alert.showAndWait();
        }else {
            listViewChatMessages.getItems().add(message);
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




}
