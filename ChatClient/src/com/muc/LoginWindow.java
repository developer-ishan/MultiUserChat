package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class LoginWindow extends JFrame{
    private ChatClient client;
    JTextField loginField;
    JPasswordField passwordField;
    JButton loginButton;

    public LoginWindow(){
        super("Login");

        this.client = new ChatClient("localhost",9000);
        client.connect();

        setUIFont (new javax.swing.plaf.FontUIResource(Font.SANS_SERIF,Font.BOLD,20));

        loginField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.add(loginField);
        p.add(passwordField);
        p.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                doLogin();
            }
        });
        getContentPane().add(p,BorderLayout.CENTER);
        pack();
        setVisible(true);
    }

    private void doLogin() {
        String login = loginField.getText();
        String password = passwordField.getText();
        try{
            if(client.login(login,password)){
                //bring user list window
                UserListPane userListPane = new UserListPane(client);

                JFrame frame = new JFrame("User List");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(400,600);
                frame.setLayout(new BorderLayout());
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        try {
                            client.logoff();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                        System.exit(0);
                    }
                });
                JPanel chatRoom = new JPanel();
                chatRoom.setPreferredSize(new Dimension(50, 100));
                JLabel l1 = new JLabel("Join a chat room",JLabel.CENTER);
                JTextField roomName = new JTextField(10);
                JButton joinButton = new JButton("Join");
                joinButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String room = roomName.getText();
                        roomName.setText("");
                        if(room.length()>=1) {
                            MessagePane messagePane = new MessagePane(client, room, MessagePane.CHAT_ROOM);
                            JFrame messageWindow = new JFrame("Room: "+room);
                            messageWindow.addWindowListener(new WindowAdapter() {
                                @Override
                                public void windowClosing(WindowEvent e) {
                                    client.leaveRoom(room);
                                    dispose();
                                }
                            });
                            messageWindow.setSize(500,500);
                            messageWindow.getContentPane().add(messagePane, BorderLayout.CENTER);
                            client.joinRoom(room);
                            messageWindow.setVisible(true);
                        } else{
                            JOptionPane.showMessageDialog(frame ,"Enter a room name first");
                        }
                    }
                });
                chatRoom.add(l1);
                chatRoom.add(roomName);
                chatRoom.add(joinButton);
                frame.getContentPane().add(chatRoom, BorderLayout.NORTH);
                frame.getContentPane().add(userListPane, BorderLayout.CENTER);
                frame.setVisible(true);

                setVisible(false);
            } else{
                //show message
                JOptionPane.showMessageDialog(this,"Invalid login/password.");
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginWindow();
            }
        });
    }

    public static void setUIFont (javax.swing.plaf.FontUIResource f){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get (key);
            if (value instanceof javax.swing.plaf.FontUIResource)
                UIManager.put (key, f);
        }
    }
}
