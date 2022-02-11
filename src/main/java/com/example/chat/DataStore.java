package com.example.chat;

import java.util.ArrayList;
import java.util.List;

public class DataStore {
    public static String username;
    public static boolean isLoggedIn = false;
    public static List<String> findFriends=new ArrayList<String>();
    public static List<String> findFriendsNoChat=new ArrayList<String>();
    public static List<String> findFriendsWithChat=new ArrayList<String>();
    public static String currentChatFriend="x";
    public static List<String> listOfMessages=new ArrayList<String>();
}
