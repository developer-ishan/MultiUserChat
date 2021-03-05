package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.Map;
import java.util.Vector;

public class UserListPane extends JPanel implements UserStatusListener{

    private ChatClient client;

    public UserListPane(ChatClient client) {
        client.addUserStatusListener(this);
        this.client = client;
        ResultSet usersHistory = client.db.getUsers();
        try {
            while (usersHistory.next()){

            }
        } catch (Exception e){
            e.printStackTrace();
        }
        setLayout(new BorderLayout());
        add(new JLabel("Users"), BorderLayout.NORTH);
    }

    @Override
    public void online(String login) {

    }

    @Override
    public void offline(String login) {

    }
}
