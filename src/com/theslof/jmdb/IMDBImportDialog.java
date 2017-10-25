package com.theslof.jmdb;

import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ResourceBundle;

public class IMDBImportDialog extends JDialog {
    private static ResourceBundle resource = ResourceBundle.getBundle("com.theslof.jmdb.resource");

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldTitle;
    private IDatabase db;

    public IMDBImportDialog(IDatabase db) {
        this.db = db;
        setContentPane(contentPane);
        setModal(true);
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
        String title = textFieldTitle.getText().trim();
        // IMDB titles are formatted as "tt" followed by seven numbers.
        if(title.length() != 9 || !title.substring(0,2).equals("tt")){
            MessageDialog.create(resource.getString("imdbInvalidTitle"));
            return;
        }
        new Thread(() -> {
            try {
                db.importFromIMDB(title);
            } catch (IOException e) {
                MessageDialog.create(resource.getString("imdbFailedImport"));
                e.printStackTrace();
            }
        }).start();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
