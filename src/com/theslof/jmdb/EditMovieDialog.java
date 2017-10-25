package com.theslof.jmdb;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Comparator;

public class EditMovieDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textFieldName;
    private JTextField textFieldYear;
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
    private JList<Person> listActors;
    private JButton buttonRemove;
    private JButton buttonAdd;
    private JTextField textFieldScore;
    private JComboBox<Person> comboBoxDirector;
    private IDatabase db;
    private Movie movie;
    private DefaultListModel<Person> actorModel = new DefaultListModel<>();
    private static final Person none = new Person("None");


    public EditMovieDialog(IDatabase db, Movie movie) {
        this.db = db;
        this.movie = movie;
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

        buttonRemove.addActionListener(e -> {
            actorModel.removeElement(listActors.getSelectedValue());
        });

        buttonAdd.addActionListener(e -> {
            AddActorDialog dialog = new AddActorDialog(db, actorModel);
            dialog.setLocationRelativeTo(this);
            dialog.pack();
            dialog.setVisible(true);
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

        textFieldName.setText(movie.getName());
        textFieldYear.setText("" + movie.getReleaseYear());
        textFieldScore.setText("" + movie.getScore());

        checkBoxAction.setSelected(movie.isGenre(Genre.ACTION));
        checkBoxAdventure.setSelected(movie.isGenre(Genre.ADVENTURE));
        checkBoxAnimation.setSelected(movie.isGenre(Genre.ANIMATION));
        checkBoxComedy.setSelected(movie.isGenre(Genre.COMEDY));
        checkBoxCrime.setSelected(movie.isGenre(Genre.CRIME));
        checkBoxDrama.setSelected(movie.isGenre(Genre.DRAMA));
        checkBoxFantasy.setSelected(movie.isGenre(Genre.FANTASY));
        checkBoxHorror.setSelected(movie.isGenre(Genre.HORROR));
        checkBoxRomance.setSelected(movie.isGenre(Genre.ROMANCE));
        checkBoxSciFi.setSelected(movie.isGenre(Genre.SCIFI));
        checkBoxThriller.setSelected(movie.isGenre(Genre.THRILLER));

        comboBoxDirector.setModel(getComboBoxModel(db.getPeople()));
        if (movie.getDirector() == null)
            comboBoxDirector.setSelectedItem(none);
        else
            comboBoxDirector.setSelectedItem(movie.getDirector());
        for (Person p : movie.getActors())
            actorModel.addElement(p);
        listActors.setModel(actorModel);

    }

    private void onOK() {
        String name = textFieldName.getText().trim();
        String year = textFieldYear.getText().trim();
        String score = textFieldScore.getText().trim();
        if (name.isEmpty() || year.isEmpty() || score.isEmpty()) {
            MessageDialog.create("Please fill in all text fields!");
            return;
        }
        try {
            movie.setName(name);
            movie.setReleaseYear(Integer.parseInt(year));
            movie.setScore((float) Double.parseDouble(score));

            movie.getGenres().clear();
            if (checkBoxAction.isSelected()) movie.addGenre(Genre.ACTION);
            if (checkBoxAdventure.isSelected()) movie.addGenre(Genre.ADVENTURE);
            if (checkBoxAnimation.isSelected()) movie.addGenre(Genre.ANIMATION);
            if (checkBoxComedy.isSelected()) movie.addGenre(Genre.COMEDY);
            if (checkBoxCrime.isSelected()) movie.addGenre(Genre.CRIME);
            if (checkBoxDrama.isSelected()) movie.addGenre(Genre.DRAMA);
            if (checkBoxFantasy.isSelected()) movie.addGenre(Genre.FANTASY);
            if (checkBoxHorror.isSelected()) movie.addGenre(Genre.HORROR);
            if (checkBoxRomance.isSelected()) movie.addGenre(Genre.ROMANCE);
            if (checkBoxSciFi.isSelected()) movie.addGenre(Genre.SCIFI);
            if (checkBoxThriller.isSelected()) movie.addGenre(Genre.THRILLER);

            Person director = (Person) comboBoxDirector.getSelectedItem();
            if (director == none || director == null)
                movie.setDirector(null);
            else
                movie.setDirector(director);

            // Clear list of actors and add all actors in the list model,
            // which saves changes made to the list of actors.
            movie.getActors().clear();
            for (int i = 0; i < listActors.getModel().getSize(); i++) {
                movie.addActor(listActors.getModel().getElementAt(i));
            }

            dispose();
        } catch (NumberFormatException e) {
            MessageDialog.create("Please input numbers for release year and score!");
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    // Generate a list of all people in the database, sorted by first name.
    private DefaultComboBoxModel<Person> getComboBoxModel(ArrayList<Person> people) {
        DefaultComboBoxModel<Person> model = new DefaultComboBoxModel<>();
        model.addElement(none);
        people.stream().sorted(Comparator.comparing(Person::getName)).forEach(model::addElement);
        return model;
    }

}
