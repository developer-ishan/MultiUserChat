/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;

/**
 *
 * @author ishan
 */
public class UserCard extends javax.swing.JPanel {

    /**
     * Creates new form UserCard
     */
    String name, status;
    private int numMsg;
    public UserCard(String name,String status, ActionListener l) {
        initComponents();
        nameJ.setActionCommand(name);
        nameJ.setText(name);
        nameJ.addActionListener(l);
        if(name.charAt(0)=='#'){
            name = name.substring(1);
        }
        this.name = name;
        this.status = status;
        this.numMsg=0;
        if(status.equals("online"))
            online();
        else
            offline();
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        statusJ = new javax.swing.JPanel();
        nameJ = new javax.swing.JButton();
        msgCountLabel = new javax.swing.JLabel();

        setBackground(new java.awt.Color(49, 47, 123));
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 10, 5));

        statusJ.setBackground(new java.awt.Color(49, 47, 123));
        statusJ.setPreferredSize(new java.awt.Dimension(14, 10));

        javax.swing.GroupLayout statusJLayout = new javax.swing.GroupLayout(statusJ);
        statusJ.setLayout(statusJLayout);
        statusJLayout.setHorizontalGroup(
            statusJLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 14, Short.MAX_VALUE)
        );
        statusJLayout.setVerticalGroup(
            statusJLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        add(statusJ);

        nameJ.setBackground(new java.awt.Color(49, 47, 123));
        nameJ.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        nameJ.setForeground(new java.awt.Color(255, 255, 255));
        nameJ.setText("jButton1");
        nameJ.setAlignmentX(0.5F);
        nameJ.setBorder(null);
        nameJ.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        nameJ.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        nameJ.setMargin(new java.awt.Insets(10, 14, 10, 14));
        add(nameJ);

        msgCountLabel.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        msgCountLabel.setForeground(new java.awt.Color(255, 255, 255));
        msgCountLabel.setText("0");
        add(msgCountLabel);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel msgCountLabel;
    private javax.swing.JButton nameJ;
    private javax.swing.JPanel statusJ;
    // End of variables declaration//GEN-END:variables

    void online() {
         statusJ.setBackground(Color.GREEN);
    }

    void offline() {
        statusJ.setBackground(Color.RED);
    }

    void newMessage() {
        numMsg++;
        msgCountLabel.setText(""+numMsg);
        System.out.println("new message called");
    }
    void readAllMessages(){
        numMsg=0;
        msgCountLabel.setText(""+numMsg);
        System.out.println("read all messages called");
    }
}