package com.theslof.jmdb;

import javax.swing.*;
import java.awt.event.*;

public class AddMovieDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldName;
    private JTextField textFieldYear;
    private JLabel labelName;
    private JLabel labelYear;
    private JCheckBox checkBoxAction;
    private JCheckBox checkBoxAdventure;
    private JCheckBox checkBoxAnimation;
    private JCheckBox checkBoxComedy;
    private JCheckBox checkBoxCrime;
    private JCheckBox checkBoxDrama;
    private JCheckBox checkBoxFantasy;
    private JCheckBox checkBoxHorror;
    private JCheckBox checkBoxRomance;
    private JCheckBox checkBoxSciFi;
    private JCheckBox checkBoxThriller;
    private IDatabase db;

    public AddMovieDialog(IDatabase db) {
        this.db = db;
        setContentPane(contentPane);
        setModal(true);
        setTitle("Add movie");
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
        String year = textFieldYear.getText().trim();
        if(name.isEmpty() || year.isEmpty()) {
            MessageDialog.create("Please fill in both a name and release year for the movie!");
            return;
        }
        try {
            Movie m = db.newMovie(name);
            m.setReleaseYear(Integer.parseInt(year));
            if(checkBoxAction.isSelected()) m.addGenre(Genre.ACTION);
            if(checkBoxAdventure.isSelected()) m.addGenre(Genre.ADVENTURE);
            if(checkBoxAnimation.isSelected()) m.addGenre(Genre.ANIMATION);
            if(checkBoxComedy.isSelected()) m.addGenre(Genre.COMEDY);
            if(checkBoxCrime.isSelected()) m.addGenre(Genre.CRIME);
            if(checkBoxDrama.isSelected()) m.addGenre(Genre.DRAMA);
            if(checkBoxFantasy.isSelected()) m.addGenre(Genre.FANTASY);
            if(checkBoxHorror.isSelected()) m.addGenre(Genre.HORROR);
            if(checkBoxRomance.isSelected()) m.addGenre(Genre.ROMANCE);
            if(checkBoxSciFi.isSelected()) m.addGenre(Genre.SCIFI);
            if(checkBoxThriller.isSelected()) m.addGenre(Genre.THRILLER);
            dispose();
        }catch (NumberFormatException e){
            textFieldYear.setText("");
            MessageDialog.create("Please input year as a number!");
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
