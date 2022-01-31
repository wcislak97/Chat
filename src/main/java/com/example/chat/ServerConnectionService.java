package com.example.chat;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnectionService implements Runnable {

    private final Socket socket;
    private final DataOutputStream dataOutputStream;
    private final DataInputStream dataInputStream;

    private final ConcurrentHashMap<Integer, ServerConnectionService> concurrentHashMap;
    private boolean isRunning;

    public ServerConnectionService(Socket socket, ConcurrentHashMap<Integer, ServerConnectionService> concurrentHashMap) throws IOException {
        this.socket = socket;
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.concurrentHashMap = concurrentHashMap;
        this.isRunning = true;
    }

    public void sendMessage(String msg) throws IOException {
        dataOutputStream.writeUTF(msg);
    }

    public void stopConnection() throws IOException {
        dataInputStream.close();
        dataOutputStream.close();
        isRunning = false;
        socket.close();
    }

    public void run() {
        while (isRunning) {
            try {
                String action = dataInputStream.readUTF();
                switch (action) {
                    case "sendMessage" -> {
                        String playerId = dataInputStream.readUTF();
                        String msg = dataInputStream.readUTF();
                        concurrentHashMap.get(Integer.parseInt(playerId)).sendMessage(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                isRunning = false;
            }
        }
//        executorService.submit(() -> {
//           while (isRunning) {
//               try {
//                   String action = dataInputStream.readUTF();
//                   switch (action) {
//                       case "sendMessage" -> {
//                           String playerId = dataInputStream.readUTF();
//                           String msg = dataInputStream.readUTF();
//                           concurrentHashMap.get(Integer.parseInt(playerId)).sendMessage(msg);
//                       }
//                   }
//               } catch (IOException e) {
//                   e.printStackTrace();
//                   isRunning = false;
//               }
//           }
//        });
    }

}
