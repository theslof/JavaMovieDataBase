/********************************
 *                              *
 *  Java Movie DataBase         *
 *  Author: Jonas Theslöf, 2017 *
 *                              *
 *            -----             *
 *                              *
 *   Start program by running   *
 *   JavaMovieDataBase.main()   *
 *      or creating a new       *
 *   JavaMovieDataBase object.  *
 *                              *
 ********************************/

package com.theslof.jmdb;

import com.theslof.jmdb.filemanagement.IWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ResourceBundle;
import java.util.concurrent.CancellationException;

import static com.theslof.jmdb.Genre.*;

public class JavaMovieDataBase {

    private static ResourceBundle resource = ResourceBundle.getBundle("com.theslof.jmdb.resource");

    private JFrame mainFrame;
    private JList<Movie> movieListContainer;
    private Database db;

    public JavaMovieDataBase() {
        db = new Database();
        EventQueue.invokeLater(this::makeGUI);
    }

    private void makeGUI() {
        // Set Look & Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Initialize the main program GUI
        mainFrame = new JFrame("JavaMovieDataBase");
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setLayout(new GridLayout());

        // Build the menu bar
        JMenuBar mainMenu = new JMenuBar();

        JMenu menuFile = new JMenu(resource.getString("file"));
        menuFile.setMnemonic(KeyEvent.VK_F);
//        JMenuItem menuPopulateDB = new JMenuItem(resource.getString("populateDB"));
        JMenuItem menuLoad = new JMenuItem(resource.getString("loadDB"));
        menuLoad.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, InputEvent.CTRL_MASK));
        JMenuItem menuSave = new JMenuItem(resource.getString("saveDB"));
        menuSave.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, InputEvent.CTRL_MASK));
        JMenu menuExport = new JMenu(resource.getString("export"));
        JMenuItem menuExportXML = new JMenuItem(resource.getString("toXML"));
        JMenuItem menuExportJSON = new JMenuItem(resource.getString("toJSON"));
        JMenuItem menuExportHTML = new JMenuItem(resource.getString("toHTML"));
        JMenuItem menuImportJSON = new JMenuItem(resource.getString("importFromJSON"));
        JMenuItem menuExit = new JMenuItem(resource.getString("exit"));
        menuExit.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Q, InputEvent.CTRL_MASK));

        JMenu menuEdit = new JMenu(resource.getString("edit"));
        menuEdit.setMnemonic(KeyEvent.VK_E);
        JMenuItem menuNewPerson = new JMenuItem(resource.getString("newPerson"));
        JMenuItem menuDeletePerson = new JMenuItem(resource.getString("deletePerson"));
        JMenuItem menuNewMovie = new JMenuItem(resource.getString("newMovie"));
        menuNewMovie.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, InputEvent.CTRL_MASK));
        JMenuItem menuDeleteMovie = new JMenuItem(resource.getString("deleteMovie"));
        menuDeleteMovie.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_D, InputEvent.CTRL_MASK));
        JMenuItem menuEditMovie = new JMenuItem(resource.getString("editMovie"));
        JMenuItem menuFromIMDB = new JMenuItem(resource.getString("fromIMDB"));
        menuFromIMDB.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_I, InputEvent.CTRL_MASK));

        JMenu menuView = new JMenu(resource.getString("view"));
        menuView.setMnemonic(KeyEvent.VK_V);
        JMenuItem menuFilter = new JMenuItem(resource.getString("filter"));
        menuFilter.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F, InputEvent.CTRL_MASK));
        JMenu menuSort = new JMenu(resource.getString("sort"));
        JMenuItem menuSortNone = new JMenuItem(resource.getString("clearSort"));
        JMenuItem menuSortName = new JMenuItem(resource.getString("byName"));
        JMenuItem menuSortYear = new JMenuItem(resource.getString("byYear"));
        JMenuItem menuSortScore = new JMenuItem(resource.getString("byScore"));

        // Set menu action listeners
//        menuPopulateDB.addActionListener(this::populateTestData);
        menuLoad.addActionListener(this::loadFromFile);
        menuSave.addActionListener(e -> saveToFile("Serialize","ser"));
        menuExportXML.addActionListener(e -> saveToFile("XML file","xml"));
        menuExportHTML.addActionListener(e -> saveToFile("HTML file","html"));
        menuExportJSON.addActionListener(e -> saveToFile("JSON file","json"));
        menuImportJSON.addActionListener(this::importFromJSON);
        menuExit.addActionListener(this::exit);

        menuNewPerson.addActionListener(this::addPerson);
        menuDeletePerson.addActionListener(this::deletePerson);
        menuNewMovie.addActionListener(this::addMovie);
        menuEditMovie.addActionListener(this::editMovie);
        menuDeleteMovie.addActionListener(this::deleteMovie);
        menuFromIMDB.addActionListener(this::newFromIMDB);

        menuFilter.addActionListener(this::filterMovies);
        menuSortNone.addActionListener(this::sortByNone);
        menuSortName.addActionListener(this::sortByName);
        menuSortYear.addActionListener(this::sortByYear);
        menuSortScore.addActionListener(this::sortByScore);

        // Link the menu objects
//        menuFile.add(menuPopulateDB);
//        menuFile.addSeparator();
        menuFile.add(menuLoad);
        menuFile.add(menuSave);
        menuFile.addSeparator();
        menuFile.add(menuExport);
        menuExport.add(menuExportJSON);
        menuExport.add(menuExportXML);
        menuExport.add(menuExportHTML);
        menuFile.add(menuImportJSON);
        menuFile.addSeparator();
        menuFile.add(menuExit);
        mainMenu.add(menuFile);
        menuEdit.add(menuNewPerson);
        menuEdit.add(menuDeletePerson);
        menuEdit.addSeparator();
        menuEdit.add(menuNewMovie);
        menuEdit.add(menuEditMovie);
        menuEdit.add(menuDeleteMovie);
        menuEdit.addSeparator();
        menuEdit.add(menuFromIMDB);
        mainMenu.add(menuEdit);
        menuView.add(menuFilter);
        menuView.add(menuSort);
        menuSort.add(menuSortNone);
        menuSort.add(menuSortName);
        menuSort.add(menuSortYear);
        menuSort.add(menuSortScore);
        mainMenu.add(menuView);
        mainFrame.setJMenuBar(mainMenu);

        // Start building the GUI
        // root holds all GUI components
        JPanel root = new JPanel();
        root.setLayout(new GridLayout(1, 2));
        //movieListContainer is the JList populated by the movieList Model object
        movieListContainer = new JList<>(db.getMovieModel());
        movieListContainer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieListContainer.addMouseListener(new MouseAdapter() {
            // Listen for double clicks on list items
            @Override
            public void mouseClicked(MouseEvent e) {
                JList<Movie> list = (JList<Movie>) e.getSource();
                if (e.getButton() == MouseEvent.BUTTON1 &&
                        e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        MovieInfoDialog dialog = new MovieInfoDialog(db.get(index));
                        dialog.setLocationRelativeTo(mainFrame);
                        dialog.pack();
                        dialog.setVisible(true);
                    }
                }
            }
        });
        //movieScrollBox makes the movieList scrollable if it exceeds the size of root
        JScrollPane movieScrollBox = new JScrollPane(movieListContainer);
        movieScrollBox.setPreferredSize(new Dimension(300, 400));
        mainFrame.add(root);
        root.add(movieScrollBox);
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    // Sort methods, setting the filter used when showing the list
    private void sortByNone(ActionEvent event) {
        db.sortByNone();
    }

    private void sortByName(ActionEvent event) {
        db.sortByName();
    }

    private void sortByYear(ActionEvent event) {
        db.sortByYear();
    }

    private void sortByScore(ActionEvent event) {
        db.sortByScore();
    }

    private void filterMovies(ActionEvent event) {
        FilterDialog dialog = new FilterDialog(db);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.pack();
        dialog.setVisible(true);
    }

    // Exit the program
    private void exit(ActionEvent event) {
        System.exit(0);
    }

    // CRUD
    private void addPerson(ActionEvent event) {
        AddPersonDialog dialog = new AddPersonDialog(db);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void deletePerson(ActionEvent event) {
        DeletePersonDialog dialog = new DeletePersonDialog(db);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void addMovie(ActionEvent event) {
        AddMovieDialog dialog = new AddMovieDialog(db);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.pack();
        dialog.setVisible(true);
    }

    private void editMovie(ActionEvent event) {
        Movie movie = movieListContainer.getSelectedValue();
        if (movie != null) {
            EditMovieDialog dialog = new EditMovieDialog(db, movie);
            dialog.setLocationRelativeTo(mainFrame);
            dialog.pack();
            dialog.setVisible(true);
        }
    }

    private void deleteMovie(ActionEvent event) {
        Movie movie = movieListContainer.getSelectedValue();
        if (movie != null) {
            db.remove(movie);
        }
    }
    // End CRUD

    // Import movie data from IMDB by specifying the IMDB ID of the movie title (ttxxxxxxx)
    private void newFromIMDB(ActionEvent event) {
        IMDBImportDialog dialog = new IMDBImportDialog(db);
        dialog.setLocationRelativeTo(mainFrame);
        dialog.pack();
        dialog.setVisible(true);
    }

    // Data management methods
    // Add default test data, used to fill DB when deserializing is not possible
    private void populateTestData(ActionEvent event) {
        db = new Database();
        movieListContainer.setModel(db.getMovieModel());

        Movie m = db.newMovie("12 Angry Men");
        m.addGenre(CRIME);
        m.addGenre(DRAMA);
        m.setReleaseYear(1957);
        m.setScore(8.9f);
        Person director = db.newPerson("Sidney Lumet");
        m.setDirector(director);
        Person lead = db.newPerson("Henry Fonda");
        m.addActor(lead);
        //------
        Person hamill = db.newPerson("Mark Hamill");
        Person ford = db.newPerson("Harrison Ford");
        Person fisher = db.newPerson("Carrie Fisher");
        Person lucas = db.newPerson("George Lucas");
        Person kershner = db.newPerson("Irvin Kershner");
        Person marquand = db.newPerson("Richard Marquand");
        //------
        Movie sw4 = db.newMovie("Star Wars: Episode IV - A New Hope");
        sw4.addGenre(ACTION);
        sw4.addGenre(ADVENTURE);
        sw4.addGenre(FANTASY);
        sw4.setReleaseYear(1977);
        sw4.setScore(8.7f);
        sw4.addActor(hamill);
        sw4.addActor(ford);
        sw4.addActor(fisher);
        sw4.setDirector(lucas);
        //------
        Movie sw5 = db.newMovie("Star Wars: Episode V - The Empire Strikes Back");
        sw5.addGenre(ACTION);
        sw5.addGenre(ADVENTURE);
        sw5.addGenre(FANTASY);
        sw5.setReleaseYear(1980);
        sw5.setScore(8.8f);
        sw5.addActor(hamill);
        sw5.addActor(ford);
        sw5.addActor(fisher);
        sw5.setDirector(kershner);
        //------
        Movie sw6 = db.newMovie("Star Wars: Episode VI - Return of the Jedi");
        sw6.addGenre(ACTION);
        sw6.addGenre(ADVENTURE);
        sw6.addGenre(FANTASY);
        sw6.setReleaseYear(1983);
        sw6.setScore(8.4f);
        sw6.addActor(hamill);
        sw6.addActor(ford);
        sw6.addActor(fisher);
        sw6.setDirector(marquand);
        //------
    }

    // Import and export from file
    private void loadFromFile(ActionEvent e) {
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Serialized file", "ser", "db"));
        int returnVal = fc.showOpenDialog(mainFrame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            new Thread(() -> {
                try {
                    db = Database.readObject(file);
                    movieListContainer.setModel(db.getMovieModel());
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (ClassNotFoundException e1) {
                    e1.printStackTrace();
                }
            }).start();
        } else {
            System.out.println("ERROR");
        }

    }

    private void saveToFile(String description, String fileType) {
        try {
            File file = SaveToFile.selectFile(description, fileType);
            new Thread(() -> {
                try {
                    IWriter<Database> writer = SaveToFile.createWriter(fileType);
                    writer.writeToFile(db, file);
                } catch (IOException e1) {
                    MessageDialog.create("Unexpected error occurred while saving data.");
                }
            }).start();
        }catch (IllegalArgumentException e1){
            MessageDialog.create("Unknown file type!");
        } catch (CancellationException e1){
        } catch (FileNotFoundException e1) {
            MessageDialog.create("Error: File not found!");
        }
    }

    private void importFromJSON(ActionEvent e) {
        final JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("JSON file", "json"));
        int returnVal = fc.showOpenDialog(mainFrame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            new Thread(() -> {
                try {
                    db = Database.readObjectJSON(file);
                    movieListContainer.setModel(db.getMovieModel());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        new JavaMovieDataBase();
    }
}
