package com.theslof.jmdb;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;

public class AddActorDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox<Person> comboBoxPerson;
    private JTextField textFieldName;
    private DefaultListModel<Person> listModel;
    private IDatabase db;
    private static final Person newPerson = new Person("< Add new person >");

    public AddActorDialog(IDatabase db, DefaultListModel<Person> listModel) {
        this.db = db;
        this.listModel = listModel;
        setContentPane(contentPane);
        setModal(true);
        setTitle("Add actor");
        getRootPane().setDefaultButton(buttonOK);
        comboBoxPerson.setModel(getModel(db.getPeople()));


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

    private DefaultComboBoxModel<Person> getModel(ArrayList<Person> people) {
        DefaultComboBoxModel<Person> model = new DefaultComboBoxModel<>();
        model.addElement(newPerson);
        people.stream().sorted(Comparator.comparing(Person::getName)).forEach(model::addElement);
        return model;
    }

    private void onOK() {
        Person selected = (Person) comboBoxPerson.getSelectedItem();
        if (selected == newPerson) {
            selected = db.newPerson(textFieldName.getText().trim());
        }
        if (!listModel.contains(selected))
            listModel.addElement(selected);

        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
