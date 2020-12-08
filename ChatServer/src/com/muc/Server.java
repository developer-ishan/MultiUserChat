package com.muc;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread{
    private final int port;

    private ArrayList<ServerWorker> workerList = new ArrayList<>();

    public Server(int port) {
        this.port = port;
    }

    public ArrayList<ServerWorker> getWorkerList() {
        return workerList;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            while (true){
                System.out.println("Listening for connection on port: "+port);
                Socket clientSocket = serverSocket.accept();
                System.out.println(clientSocket+" has connected.");
                ServerWorker worker = new ServerWorker(this ,clientSocket);
                Thread workerThread = new Thread(worker);
                workerList.add(worker);
                workerThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
