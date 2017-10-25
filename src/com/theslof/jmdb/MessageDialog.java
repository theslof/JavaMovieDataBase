package com.theslof.jmdb;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel dialogText;

    private MessageDialog(String message) {
        setContentPane(contentPane);
        dialogText.setText(message);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public static MessageDialog create(String message){
        MessageDialog dialog = new MessageDialog(message);
        dialog.setLocationRelativeTo(null);
        dialog.pack();
        dialog.setVisible(true);
        return dialog;
    }
}
