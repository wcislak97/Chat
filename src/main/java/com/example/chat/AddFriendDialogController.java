package com.example.chat;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import static com.example.chat.ClientConnectionService.sendMessage;

public class AddFriendDialogController {

    private static String choseFriendToBeAdded=null;

    @FXML
    private Button btnFindFriend;

    @FXML
    private Button btnStartNewChat;

    @FXML
    private TextField txtFindFriend;

    @FXML
    private ListView listViewAddFriends;

    @FXML
    private void onbtnStartNewChatClicked(){
    if(choseFriendToBeAdded!= null && !choseFriendToBeAdded.isEmpty()){
        JSONObject jo = new JSONObject();
        jo.put("operation", "addFriend");
        jo.put("username",DataStore.username);
        jo.put("friend",choseFriendToBeAdded);
        sendMessage(jo.toString());
        System.out.println("Wyslano dodanie przyjaciela");
        onbtnFindFriendClicked();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Przyjaciel dodany");
        alert.showAndWait();

    }
    else{
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nie wybrano przyjaciela do dodania");
        alert.showAndWait();
    }

    }

    @FXML
    private void onCellClicked(){
        choseFriendToBeAdded= (String) listViewAddFriends.getSelectionModel().getSelectedItem();
        System.out.println(choseFriendToBeAdded + " clicked");
    }

    @FXML
    private void onbtnFindFriendClicked(){
        String friend=txtFindFriend.getText();

        if(friend.isEmpty() || friend.isBlank()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Some info is lacking");
            alert.showAndWait();
        }
        else{
            JSONObject jo = new JSONObject();
            jo.put("operation", "findFriends");
            jo.put("username",DataStore.username);
            jo.put("friend",friend);


            sendMessage(jo.toString());
            System.out.println("Wyslano przyjaciela");
            listViewAddFriends.getItems().clear();

            for(int i=0;i<DataStore.findFriends.size();i++) {
                System.out.println(DataStore.findFriends.get(i).toString());
                listViewAddFriends.getItems().add(DataStore.findFriends.get(i).toString());
            }
        }

    }



}
