package com.muc;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ChatClient {

    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;

    private ArrayList<UserStatusListener> userStatusListeners = new ArrayList<>();
    private ArrayList<MessageListener> messageListeners = new ArrayList<>();

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public static void main(String[] args) throws IOException{
        ChatClient client = new ChatClient("localhost",8018);
        client.addUserStatusListener(new UserStatusListener() {
            @Override
            public void online(String login) {
                System.out.println("ONLINE: "+login);
            }

            @Override
            public void offline(String login) {
                System.out.println("OFFLINE: "+login);
            }
        });

        client.addMessageListener(new MessageListener() {
            @Override
            public void onMessage(String from, String msgBody) {
                System.out.println("You got a message from "+from+" ===> "+msgBody );
            }
        });

        if (client.connect()){
            System.out.println("Connected Successfully");

            if(client.login("guest","guest")){
                 System.out.println("Login Successful.");
                 
                 client.msg("ishan", "Hello World");

//                 client.logoff();
            } else{
                System.err.println("Login Failed.");
            }

        } else{
            System.out.println("Connection failed");
        }
    }

    private void msg(String sendTo, String msgBody) throws IOException {
        String cmd = "msg " + sendTo + " " + msgBody + "\n";
        serverOut.write(cmd.getBytes());
    }

    private void logoff() throws IOException{
        String cmd = "logoff\n";
        serverOut.write(cmd.getBytes());
    }

    private boolean login(String login, String password) throws IOException{
        String cmd = "login " + login + " " + password + "\n";
        serverOut.write(cmd.getBytes());

        String response = bufferedIn.readLine();
        System.out.println("Response: "+response);

        if("ok login".equalsIgnoreCase(response)){
            startMessageReader();
            return true;
        } else{
            return false;
        }
    }

    private void startMessageReader() {
        Thread t = new Thread(){
            @Override
            public void run() {
                readMessageLoop();
            }
        };
        t.start();
    }

    private void readMessageLoop(){
        try {
            String line;
            // when socket is closed then the loop also breaks
            // as read line will return null
            while ( (line = bufferedIn.readLine()) != null ){
                String[] tokens = StringUtils.split(line);
                if (tokens != null && tokens.length > 0) {
                    String cmd = tokens[0];

                    if("online".equalsIgnoreCase(cmd)){
                        handleOnline(tokens);
                    } else if("offline".equalsIgnoreCase(cmd)){
                        handleOffline(tokens);
                    } else if("msg".equalsIgnoreCase(cmd)){
                        String[] tokensMsg = StringUtils.split(line, null, 3);
                        handleMessage(tokensMsg);
                    }
                }
            }
        } catch (IOException e){
            e.printStackTrace();
            try {
                socket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private void handleMessage(String[] tokensMsg) {
        String from = tokensMsg[1];
        String msgBody = tokensMsg[2];

        for (MessageListener listener : messageListeners){
            listener.onMessage(from,msgBody);
        }
    }

    private void handleOffline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners){
            listener.offline(login);
        }
    }

    //call the online method of all the registered user status listeners
    private void handleOnline(String[] tokens) {
        String login = tokens[1];
        for(UserStatusListener listener : userStatusListeners){
            listener.online(login);
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

    public void addUserStatusListener(UserStatusListener listener){
        userStatusListeners.add(listener);
    }

    public void removeUserStatusListener(UserStatusListener listener){
        userStatusListeners.add(listener);
    }

    public void addMessageListener(MessageListener listener){
        messageListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener){
        messageListeners.remove(listener);
    }
}