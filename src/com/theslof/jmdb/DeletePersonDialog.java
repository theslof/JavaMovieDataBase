package com.theslof.jmdb;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DeletePersonDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<Person> listActors;
    private IDatabase db;

    public DeletePersonDialog(IDatabase db) {
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

        DefaultListModel<Person> actorModel = new DefaultListModel<>();
        // Sort the list of all people and add each person to the list model:
        db.getPeople().stream() // Convert the list of all people to a stream
                .sorted(Comparator.comparing(Person::getName)) // Sort the stream based on the name of each person
                .forEach(actorModel::addElement) // Add each person to the list model
        ;

        // Add the list model to the JList
        listActors.setModel(actorModel);

    }

    private void onOK() {
        // Get a list of all selected people in our JList,
        // and call db.remove(p) for each Person p
        listActors.getSelectedValuesList().forEach(db::remove);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
