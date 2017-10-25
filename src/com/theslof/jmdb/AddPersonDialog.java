package com.theslof.jmdb;

import javax.swing.*;
import java.awt.event.*;

public class AddPersonDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldName;
    private IDatabase db;

    public AddPersonDialog(IDatabase db) {
        this.db = db;
        setContentPane(contentPane);
        setModal(true);
        setTitle("Add person");
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String name = textFieldName.getText().trim();
        if(!name.isEmpty()) {
            db.newPerson(name);
            dispose();
        }else{
            MessageDialog.create("Please fill in a name!");
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
