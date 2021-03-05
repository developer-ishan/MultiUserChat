package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;

public class MessagePane extends JPanel implements MessageListener{

    private ChatClient client;
    private String user;
    private int type;
    final static int DM = 0;
    final static int CHAT_ROOM = 1;

    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> messageList = new JList<>(listModel);
    private JTextField inputField = new JTextField();

    public MessagePane(ChatClient client, String receiver, int type) {
        this.client = client;
        this.type = type;
        if(type==DM){
            this.user = receiver;
        } else {
            this.user = "#"+receiver;
            System.out.println("Chat room " + this.user);
        }

        client.addMessageListener(this);

        setLayout(new BorderLayout());
        add(new JScrollPane(messageList),BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

        ResultSet messageHistory = client.db.getMessages(user);
        try {
            if(messageHistory!=null)
            while (messageHistory.next()){
                if(messageHistory.getInt("direction")==1){
                    listModel.addElement("you: "+messageHistory.getString("message"));
                }
                else{
                    String line = messageHistory.getString("name") + ": " + messageHistory.getString("message");
                    listModel.addElement(line);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    String text = inputField.getText();
                    client.msg(user, text);
                    listModel.addElement("you: "+text);
                    inputField.setText("");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onMessage(String from, String msgBody) {
        String line = from + ": " + msgBody;
        listModel.addElement(line);
    }
}
