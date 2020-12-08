package com.muc;

import java.io.*;
import java.net.Socket;

public class ChatClient {

    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException{
        ChatClient client = new ChatClient("localhost",8018);
        if (client.connect()){
            System.out.println("Connected Successfully");

            if(client.login("guest","guest")){
                 System.out.println("Login Successful.");
            } else{
                System.err.println("Login Failed.");
            }

        } else{
            System.out.println("Connection failed");
        }
    }

    private boolean login(String login, String password) throws IOException{
        String cmd = "login " + login + " " + password + "\n";
        serverOut.write(cmd.getBytes());

        String response = bufferedIn.readLine();
        System.out.println("Response: "+response);

        if("ok login".equalsIgnoreCase(response)){
            return true;
        } else{
            return false;
        }
    }

    private boolean connect() {
        try {
            this.socket = new Socket(serverName,serverPort);
            System.out.println("Client port is "+socket.getLocalPort());
            this.serverOut = socket.getOutputStream();
            this.serverIn = socket.getInputStream();
            this.bufferedIn = new BufferedReader(new InputStreamReader(serverIn));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
