package com.example.chat;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final ConcurrentHashMap<Integer, ServerConnectionService> concurrentHashMap = new ConcurrentHashMap<>();
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);
    private static int id = 0;

    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(9999);
            while (true) {
                System.out.println("Czekam na polaczenie...");
                Socket socket = serverSocket.accept();
                System.out.println("Nawiazano polaczenie.");

                ServerConnectionService serverConnectionService = new ServerConnectionService(socket, concurrentHashMap);
                concurrentHashMap.put(id, serverConnectionService);
                id++;

                executorService.submit(serverConnectionService);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}