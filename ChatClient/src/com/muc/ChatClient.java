package com.muc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ChatClient {

    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) {
        ChatClient client = new ChatClient("localhost",8818);
        if (client.connect()){
            System.out.println("Connected Successfully");
        } else{
            System.out.println("Connection failed");
        }
    }

    private boolean connect() {
        try {
            this.socket = new Socket(serverName,serverPort);
            System.out.println("Client port is "+socket.getLocalPort());
            this.serverOut = socket.getOutputStream();
            this.serverIn = socket.getInputStream();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
