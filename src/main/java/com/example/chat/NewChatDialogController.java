package com.example.chat;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import org.json.JSONObject;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.ResourceBundle;

import static com.example.chat.ClientConnectionService.sendMessage;

public class NewChatDialogController implements Initializable, PropertyChangeListener {

    private static String choseChatToBeAdded=null;
    private ObservableList<String> observableList = FXCollections.observableArrayList();

    @FXML
    private Button btnStartNewChat;

    @FXML
    private ScrollPane newChatPane;

    @FXML
    private ListView listViewFriends;



    @FXML
    private void onCellClicked(){
        choseChatToBeAdded= (String) listViewFriends.getSelectionModel().getSelectedItem();
        System.out.println(choseChatToBeAdded + " clicked");
    }

    @FXML
    private void onbtnStartNewChatClicked(){
        if(choseChatToBeAdded!= null && !choseChatToBeAdded.isEmpty()){
            JSONObject jo = new JSONObject();
            jo.put("operation", "addChat");
            jo.put("username",DataStore.username);
            jo.put("friend",choseChatToBeAdded);
            sendMessage(jo.toString());
            System.out.println("Wyslano dodanie chatu z przyjacielem");
            getFriendsList();
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Chat z przyjacielem dodany");
            alert.showAndWait();

        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Nie wybrano przyjaciela do chatowania");
            alert.showAndWait();
        }
    }


    private void getFriendsList(){
        JSONObject jo = new JSONObject();
        jo.put("operation", "findFriendsNoChat");
        jo.put("username",DataStore.username);

        sendMessage(jo.toString());
        System.out.println("Wyslano przyjaciela");
        listViewFriends.getItems().clear();
        for(int i=0;i<DataStore.findFriendsNoChat.size();i++) {
            System.out.println(DataStore.findFriendsNoChat.get(i).toString());
            listViewFriends.getItems().add(DataStore.findFriendsNoChat.get(i).toString());
        }

}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getFriendsList();

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Platform.runLater(() -> {
            listViewFriends.getItems().add(evt.getNewValue());
        });
    }
}
