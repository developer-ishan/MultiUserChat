package com.muc;

import org.apache.commons.lang3.StringUtils;

import javax.xml.crypto.Data;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;

public class ServerWorker implements Runnable{

    private final Socket clientSocket;
    private final Server server;
    private String login = null;
    private OutputStream outputStream;

    public ServerWorker(Server server ,Socket clientSocket){
        this.server = server;
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            handleClientSocket();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void handleClientSocket() throws IOException, InterruptedException{
        InputStream inputStream = clientSocket.getInputStream();
        this.outputStream = clientSocket.getOutputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ( (line = reader.readLine()) != null ){
            String[] tokens = StringUtils.split(line);
            if(tokens != null && tokens.length > 0){
                String cmd = tokens[0];
                if("quit".equalsIgnoreCase(cmd)){
                    break;
                } else if("login".equalsIgnoreCase(cmd)){
                    handleLogin(outputStream, tokens);
                }
                else {
                    String msg = "Unknown option "+cmd+"\n";
                    outputStream.write(msg.getBytes());
                }
            }
        }
        clientSocket.close();
    }

    public String getLogin() {
        return login;
    }

    private void handleLogin(OutputStream outputStream, String[] tokens) throws IOException {
        String msg = null;
        if(tokens.length == 3){
            String login = tokens[1];
            String password = tokens[2];

            if((login.equals("guest") && password.equals("guest")) ||
                    (login.equals("ishan") && password.equals("ishan")) ||
                    (login.equals("mojo") && password.equals("mojo")) ||
                    (login.equals("devel") && password.equals("devel"))){
                msg = "OK LOGIN\n";
                this.login = login;
                System.out.println("User logged in successfully: "+login);

                String onLineMsg = "ONLINE "+login+"\n";
                ArrayList<ServerWorker> workerList = server.getWorkerList();
                for(ServerWorker worker : workerList){
                    worker.send(onLineMsg);
                }
            } else {
                msg = "ERROR LOGIN\n";
            }
        } else{
            msg = "INVALID arguments should be [login <user> <password>]\n";
        }
        outputStream.write(msg.getBytes());
    }

    private void send(String msg) throws IOException{
        outputStream.write(msg.getBytes());
    }
}
