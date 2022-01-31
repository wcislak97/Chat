package com.example.chat;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientConnectionService implements Runnable {

    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    private final PropertyChangeSupport propertyChangeSupport;
    private String lastMessage;
    private final ExecutorService executorService;

    public ClientConnectionService() throws IOException {
        this.socket = new Socket("localhost", 9999);
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void sendMessage(String msg) throws IOException {
        dataOutputStream.writeUTF("sendMessage");
        dataOutputStream.writeUTF("0");
        dataOutputStream.writeUTF(msg);
    }

    public void addListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    public void stopConnection() {
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        executorService.submit(() -> {
            while (true) {
                String newText = dataInputStream.readUTF();
                propertyChangeSupport.firePropertyChange("newText", lastMessage, newText);
                System.out.println(newText);
                lastMessage = newText;
            }
        });
    }

}
