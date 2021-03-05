package com.muc;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class ChatClient {

    private final String serverName;
    private final int serverPort;
    private Socket socket;
    private InputStream serverIn;
    private OutputStream serverOut;
    private BufferedReader bufferedIn;
    public Database db;

    private Vector<UserStatusListener> userStatusListeners = new Vector<>();
    private Vector<MessageListener> messageListeners = new Vector<>();

    public ChatClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    public void msg(String sendTo, String msgBody) throws IOException {
        String cmd = "msg " + sendTo + " " + msgBody + "\n";
        try {
            db.message(sendTo, msgBody, Database.SENT);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        serverOut.write(cmd.getBytes());
    }

    public void logoff() throws IOException{
        String cmd = "logoff\n";
        serverOut.write(cmd.getBytes());
    }

    public boolean login(String login, String password) throws IOException{
        String cmd = "login " + login + " " + password + "\n";
        serverOut.write(cmd.getBytes());
        String response = bufferedIn.readLine();
        if("ok login".equalsIgnoreCase(response)){
            db = new Database(login);
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
                    System.out.println(cmd);
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
            try {
                db.message(from, msgBody, Database.RECEIVED);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
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
        System.out.println("handling login called");
        for(UserStatusListener listener : userStatusListeners){
            listener.online(login);
            System.out.println(listener+" called");
        }
    }

    public boolean connect() {
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
        System.out.println("UserStatus Listener added");
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

    public void joinRoom(String room){
        String cmd = "join #" + room + "\n";
        try {
            serverOut.write(cmd.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void leaveRoom(String room) {
        String cmd = "leave #" + room + "\n";
        try {
            serverOut.write(cmd.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}