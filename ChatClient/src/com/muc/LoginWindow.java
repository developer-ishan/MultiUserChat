package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LoginWindow extends JFrame {
    private final ChatClient client;
    JTextField loginField;
    JPasswordField passwordField;
    JButton loginButton;

    public LoginWindow(){
        super("Login");

        this.client = new ChatClient("localhost",8018);
        client.connect();

        setUIFont (new javax.swing.plaf.FontUIResource(Font.SANS_SERIF,Font.BOLD,20));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
        LoginWindow loginWindow = new LoginWindow();
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
