import dto.AuthorDTO;
import dto.BookDTO;
import rmi.Backend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class App extends JFrame {
    private static JFrame frame;

    private static Backend backend = null;

    private static AuthorDTO currentAuthor = null;
    private static BookDTO currentBook = null;

    private static boolean editMode = false;
    private static boolean authorMode = true;

    private static JButton btnAddAuthor = new JButton("Add Author");
    private static JButton btnAddBook = new JButton("Add Book");
    private static JButton btnEdit = new JButton("Edit Data");
    private static JButton btnBack = new JButton("Back");
    private static JButton btnSave = new JButton("Save");
    private static JButton btnDelete = new JButton("Delete");

    private static Box menuPanel = Box.createVerticalBox();
    private static Box actionPanel = Box.createVerticalBox();
    private static Box comboPanel = Box.createVerticalBox();
    private static Box bookPanel = Box.createVerticalBox();
    private static Box authorPanel = Box.createVerticalBox();

    private static JComboBox comboAuthor = new JComboBox();
    private static JComboBox comboBook = new JComboBox();

    private static JTextField textAuthorName = new JTextField(30);
    private static JTextField textBookName = new JTextField(30);
    private static JTextField textBookAuthorName = new JTextField(30);
    private static JTextField textBookYear = new JTextField(30);

    private App() {
        super("Library");
        frame = this;
        frame.setPreferredSize(new Dimension(400, 500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                frame.dispose();
                System.exit(0);
            }
        });
        Box box = Box.createVerticalBox();
        sizeAllElements();
        frame.setLayout(new FlowLayout());

        // Menu
        menuPanel.add(btnAddAuthor);
        menuPanel.add(Box.createVerticalStrut(20));
        btnAddAuthor.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = false;
                authorMode = true;
                menuPanel.setVisible(false);
                comboPanel.setVisible(false);
                authorPanel.setVisible(true);
                bookPanel.setVisible(false);
                actionPanel.setVisible(true);
                pack();
            }
        });
        menuPanel.add(btnAddBook);
        menuPanel.add(Box.createVerticalStrut(20));
        btnAddBook.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = false;
                authorMode = false;
                menuPanel.setVisible(false);
                comboPanel.setVisible(false);
                authorPanel.setVisible(false);
                bookPanel.setVisible(true);
                actionPanel.setVisible(true);
                pack();
            }
        });
        menuPanel.add(btnEdit);
        menuPanel.add(Box.createVerticalStrut(20));
        btnEdit.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                editMode = true;
                menuPanel.setVisible(false);
                comboPanel.setVisible(true);
                authorPanel.setVisible(false);
                bookPanel.setVisible(false);
                actionPanel.setVisible(true);
                pack();
            }
        });

        // ComboBoxes
        comboPanel.add(new JLabel("Author:"));
        comboPanel.add(comboAuthor);
        comboPanel.add(Box.createVerticalStrut(20));
        comboAuthor.addActionListener(e -> {
            String name = (String) comboAuthor.getSelectedItem();
            try {
                currentAuthor = backend.authorFindByName(name);
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            authorMode = true;
            authorPanel.setVisible(true);
            bookPanel.setVisible(false);
            fillAuthorFields();
            pack();
        });
        comboPanel.add(new JLabel("Book:"));
        comboPanel.add(comboBook);
        comboPanel.add(Box.createVerticalStrut(20));
        comboBook.addActionListener(e -> {
            String name = (String) comboBook.getSelectedItem();
            try {
                currentBook = backend.bookFindByName(name);
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            authorMode = false;
            authorPanel.setVisible(false);
            bookPanel.setVisible(true);
            try {
                fillBookFields();
            } catch (RemoteException remoteException) {
                remoteException.printStackTrace();
            }
            pack();
        });
        try {
            fillComboBoxes();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        comboPanel.setVisible(false);

        bookPanel.add(new JLabel("Name:"));
        bookPanel.add(textBookName);
        bookPanel.add(Box.createVerticalStrut(20));
        bookPanel.add(new JLabel("Author Name:"));
        bookPanel.add(textBookAuthorName);
        bookPanel.add(Box.createVerticalStrut(20));
        bookPanel.add(new JLabel("Release year:"));
        bookPanel.add(textBookYear);
        bookPanel.add(Box.createVerticalStrut(20));
        bookPanel.setVisible(false);

        authorPanel.add(new JLabel("Name:"));
        authorPanel.add(textAuthorName);
        authorPanel.add(Box.createVerticalStrut(20));
        authorPanel.setVisible(false);

        actionPanel.add(btnSave);
        actionPanel.add(Box.createVerticalStrut(20));
        btnSave.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                save();
            }
        });
        actionPanel.add(btnDelete);
        actionPanel.add(Box.createVerticalStrut(20));
        btnDelete.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                try {
                    delete();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        actionPanel.add(btnBack);
        actionPanel.add(Box.createVerticalStrut(20));
        btnBack.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                clearFields();
                menuPanel.setVisible(true);
                comboPanel.setVisible(false);
                authorPanel.setVisible(false);
                bookPanel.setVisible(false);
                actionPanel.setVisible(false);
                pack();
            }
        });
        actionPanel.setVisible(false);

        clearFields();
        box.setPreferredSize(new Dimension(300, 500));
        box.add(menuPanel);
        box.add(comboPanel);
        box.add(authorPanel);
        box.add(bookPanel);
        box.add(actionPanel);
        setContentPane(box);
        pack();
    }

    private static void sizeAllElements() {
        Dimension dimension = new Dimension(300, 50);
        btnAddAuthor.setMaximumSize(dimension);
        btnAddBook.setMaximumSize(dimension);
        btnEdit.setMaximumSize(dimension);
        btnBack.setMaximumSize(dimension);
        btnSave.setMaximumSize(dimension);
        btnDelete.setMaximumSize(dimension);

        btnAddAuthor.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAddBook.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSave.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBack.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEdit.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDelete.setAlignmentX(Component.CENTER_ALIGNMENT);

        Dimension panelDimension = new Dimension(300, 300);
        menuPanel.setMaximumSize(panelDimension);
        comboPanel.setPreferredSize(panelDimension);
        actionPanel.setPreferredSize(panelDimension);
        bookPanel.setPreferredSize(panelDimension);
        authorPanel.setPreferredSize(panelDimension);

        comboAuthor.setPreferredSize(dimension);
        comboBook.setPreferredSize(dimension);

        textBookAuthorName.setPreferredSize(dimension);
        textBookName.setPreferredSize(dimension);
        textBookYear.setPreferredSize(dimension);
        textAuthorName.setPreferredSize(dimension);
    }

    private static void save() {
        if (editMode) {
            if (authorMode) {
                currentAuthor.setName(textAuthorName.getText());
                try {
                    if (backend.authorUpdate(currentAuthor)) {
                        JOptionPane.showMessageDialog(null, "Error: update failed!");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else {
                currentBook.setName(textBookName.getText());
                currentBook.setReleaseYear(Integer.parseInt(textBookYear.getText()));

                AuthorDTO author = null;
                try {
                    author = backend.authorFindByName(textBookAuthorName.getText());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (author == null) {
                    JOptionPane.showMessageDialog(null, "Error: no such author!");
                    return;
                }
                currentBook.setAuthorId(author.getId());

                try {
                    if (!backend.bookUpdate(currentBook)) {
                        JOptionPane.showMessageDialog(null, "Error: update failed!");
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (authorMode) {
                AuthorDTO author = new AuthorDTO();
                author.setName(textAuthorName.getText());
                try {
                    if (!backend.authorInsert(author)) {
                        JOptionPane.showMessageDialog(null, "Error: insertion failed!");
                        return;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                comboAuthor.addItem(author.getName());
            } else {
                BookDTO book = new BookDTO();
                book.setName(textBookName.getText());
                book.setReleaseYear(Integer.parseInt(textBookYear.getText()));

                AuthorDTO author = null;
                try {
                    author = backend.authorFindByName(textBookAuthorName.getText());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if (author == null) {
                    JOptionPane.showMessageDialog(null, "Error: no such author!");
                    return;
                }
                book.setAuthorId(author.getId());

                try {
                    if (!backend.bookInsert(book)) {
                        JOptionPane.showMessageDialog(null, "Error: insertion failed!");
                        return;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                comboBook.addItem(book.getName());
            }
        }
    }

    private static void delete() throws RemoteException {
        if (editMode) {
            if (authorMode) {
                List<BookDTO> list = backend.bookFindByAuthorId(currentAuthor.getId());
                assert list != null;
                for (BookDTO m : list) {
                    comboBook.removeItem(m.getName());
                    backend.bookDelete(m);
                }
                backend.authorDelete(currentAuthor);
                comboAuthor.removeItem(currentAuthor.getName());
            } else {
                backend.bookDelete(currentBook);
                comboBook.removeItem(currentBook.getName());
            }
        }
    }

    private void fillComboBoxes() throws RemoteException {
        comboAuthor.removeAllItems();
        comboBook.removeAllItems();
        List<AuthorDTO> authors = backend.authorFindAll();
        List<BookDTO> books = backend.bookFindAll();
        assert authors != null;
        for (AuthorDTO author : authors) {
            comboAuthor.addItem(author.getName());
        }
        assert books != null;
        for (BookDTO book : books) {
            comboBook.addItem(book.getName());
        }
    }

    private static void clearFields() {
        textAuthorName.setText("");
        textBookName.setText("");
        textBookAuthorName.setText("");
        textBookYear.setText("");
        currentAuthor = null;
        currentBook = null;
    }

    private static void fillAuthorFields() {
        if (currentAuthor == null)
            return;
        textAuthorName.setText(currentAuthor.getName());
    }

    private static void fillBookFields() throws RemoteException {
        if (currentBook == null)
            return;
        AuthorDTO author = backend.authorFindById(currentBook.getAuthorId());
        textBookName.setText(currentBook.getName());
        assert author != null;
        textBookAuthorName.setText(author.getName());
        textBookYear.setText(String.valueOf(currentBook.getReleaseYear()));
    }

    public static void main(String[] args) throws IOException, NotBoundException {
        String url = "//localhost:1234/videoshop";
        backend = (Backend) Naming.lookup(url);
        JFrame myWindow = new App();
        myWindow.setVisible(true);
    }
}
