package com.theslof.jmdb;

import com.theslof.jmdb.filters.IFilter;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FilterDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel labelFilterString;
    private JTextField textFieldString;
    private JLabel labelPerson;
    private JComboBox<Person> comboBoxPerson;
    private JLabel labelGenres;
    private JCheckBox checkBoxAll;
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
    private static final Person none = new Person("All");
    private JCheckBox[] checkBoxes = {checkBoxAction, checkBoxAdventure, checkBoxAnimation, checkBoxComedy,
            checkBoxCrime, checkBoxDrama, checkBoxFantasy, checkBoxHorror, checkBoxRomance, checkBoxSciFi, checkBoxThriller};

    public FilterDialog(IDatabase db) {
        this.db = db;
        setContentPane(contentPane);
        setModal(true);
        setTitle("Filter");
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

        for (JCheckBox box : checkBoxes) {
            box.addActionListener(e -> {
                if (!box.isSelected() && checkBoxAll.isSelected())
                    checkBoxAll.setSelected(false);
                else if (allSelected())
                    checkBoxAll.setSelected(true);
            });

        }

        checkBoxAll.addActionListener(e -> {
            if (checkBoxAll.isSelected()) {
                for (JCheckBox box : checkBoxes)
                    box.setSelected(true);
            } else {
                if (allSelected())
                    for (JCheckBox box : checkBoxes)
                        box.setSelected(false);
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
        model.addElement(none);
        people.stream().sorted(Comparator.comparing(Person::getName)).forEach(model::addElement);
        return model;
    }

    private void onOK() {
        String s = textFieldString.getText().trim();
        Person p = (Person) comboBoxPerson.getSelectedItem();
        p = (p != none) ? p : null;
        ArrayList<Genre> genres = getSelectedGenres();
        ArrayList<IFilter<Movie>> filters = db.getFilter();
        filters.clear();
        if (!genres.isEmpty())
            filters.add(Factory.filter(genres));
        if (p != null)
            filters.add(Factory.filter(p));
        if (!s.isEmpty())
            filters.add(Factory.filter(s));
        db.updateModel();
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private ArrayList<Genre> getSelectedGenres() {
        ArrayList<Genre> genres = new ArrayList<>();
        if (checkBoxAll.isSelected())
            return genres;
        if (checkBoxAction.isSelected()) genres.add(Genre.ACTION);
        if (checkBoxAdventure.isSelected()) genres.add(Genre.ADVENTURE);
        if (checkBoxAnimation.isSelected()) genres.add(Genre.ANIMATION);
        if (checkBoxComedy.isSelected()) genres.add(Genre.COMEDY);
        if (checkBoxCrime.isSelected()) genres.add(Genre.CRIME);
        if (checkBoxDrama.isSelected()) genres.add(Genre.DRAMA);
        if (checkBoxFantasy.isSelected()) genres.add(Genre.FANTASY);
        if (checkBoxHorror.isSelected()) genres.add(Genre.HORROR);
        if (checkBoxRomance.isSelected()) genres.add(Genre.ROMANCE);
        if (checkBoxSciFi.isSelected()) genres.add(Genre.SCIFI);
        if (checkBoxThriller.isSelected()) genres.add(Genre.THRILLER);
        return genres;
    }

    private void setSelectedGenres(ArrayList<Genre> genres) {
        if (genres.isEmpty())
            return;
        else
            checkBoxAll.setSelected(false);
        checkBoxAction.setSelected(genres.contains(Genre.ACTION));
        checkBoxAdventure.setSelected(genres.contains(Genre.ADVENTURE));
        checkBoxAnimation.setSelected(genres.contains(Genre.ANIMATION));
        checkBoxComedy.setSelected(genres.contains(Genre.COMEDY));
        checkBoxCrime.setSelected(genres.contains(Genre.CRIME));
        checkBoxDrama.setSelected(genres.contains(Genre.DRAMA));
        checkBoxFantasy.setSelected(genres.contains(Genre.FANTASY));
        checkBoxHorror.setSelected(genres.contains(Genre.HORROR));
        checkBoxRomance.setSelected(genres.contains(Genre.ROMANCE));
        checkBoxSciFi.setSelected(genres.contains(Genre.SCIFI));
        checkBoxThriller.setSelected(genres.contains(Genre.THRILLER));

    }

    public boolean allSelected() {
        for (JCheckBox box : checkBoxes) {
            if (!box.isSelected())
                return false;
        }
        return true;
    }
}
