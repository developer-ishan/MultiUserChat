package com.muc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MessagePane extends JPanel implements MessageListener{

    private final ChatClient client;
    private final String user;

    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> messageList = new JList<>(listModel);
    private JTextField inputField = new JTextField();

    public MessagePane(ChatClient client, String user) {
        this.client = client;
        this.user = user;

        client.addMessageListener(this);

        setLayout(new BorderLayout());
        add(new JScrollPane(messageList),BorderLayout.CENTER);
        add(inputField, BorderLayout.SOUTH);

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
