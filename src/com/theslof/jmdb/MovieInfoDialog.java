package com.theslof.jmdb;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.StringJoiner;

public class MovieInfoDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel labelName;
    private JLabel labelYear;
    private JLabel labelGenres;
    private JLabel labelDirector;
    private JLabel labelActors;
    private JLabel labelScore;

    public MovieInfoDialog(Movie movie) {
        setContentPane(contentPane);
        setModal(true);
        setMinimumSize(new Dimension(300,200));
        getRootPane().setDefaultButton(buttonOK);

        labelName.setText(movie.getName());
        labelScore.setText(movie.getScore() + "/10");
        labelYear.setText("" + movie.getReleaseYear());
        labelGenres.setText(movie.genresAsString());
        if(movie.getDirector() != null)
            labelDirector.setText(movie.getDirector().getName());

        StringJoiner s = new StringJoiner("<br />");
        for (Person p:movie.getActors()) {
            s.add(p.getName());
        }
        labelActors.setText("<html>" + s.toString());

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
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
