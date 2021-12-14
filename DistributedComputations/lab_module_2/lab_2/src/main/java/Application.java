import connection.CustomConnection;
import dao.AuthorDAO;
import dao.BookDAO;
import dto.AuthorDTO;
import dto.BookDTO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class Application extends JFrame {
    private static JFrame frame;

    private static AuthorDTO currentauthor = null;
    private static BookDTO currentbook = null;

    private static boolean editMode = false;
    private static boolean authorMode = true;

    private static JButton btnAddauthor = new JButton("Add author");
    private static JButton btnAddbook = new JButton("Add book");
    private static JButton btnEdit = new JButton("Edit Data");
    private static JButton btnBack = new JButton("Back");
    private static JButton btnSave = new JButton("Save");
    private static JButton btnDelete = new JButton("Delete");

    private static Box menuPanel = Box.createVerticalBox();
    private static Box actionPanel = Box.createVerticalBox();
    private static Box comboPanel = Box.createVerticalBox();
    private static Box bookPanel = Box.createVerticalBox();
    private static Box authorPanel = Box.createVerticalBox();

    private static JComboBox comboauthor = new JComboBox();
    private static JComboBox combobook = new JComboBox();

    private static JTextField textauthorName = new JTextField(30);
    private static JTextField textbookName = new JTextField(30);
    private static JTextField textbookauthorName = new JTextField(30);
    private static JTextField textbookYear = new JTextField(30);

    private Application() {
        super("Library");
        frame = this;
        frame.setPreferredSize(new Dimension(400, 500));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                frame.dispose();
                CustomConnection.closeConnection();
                System.exit(0);
            }
        });
        Box box = Box.createVerticalBox();
        sizeAllElements();
        frame.setLayout(new FlowLayout());

        // Menu
        menuPanel.add(btnAddauthor);
        menuPanel.add(Box.createVerticalStrut(20));
        btnAddauthor.addMouseListener(new MouseAdapter() {
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
        menuPanel.add(btnAddbook);
        menuPanel.add(Box.createVerticalStrut(20));
        btnAddbook.addMouseListener(new MouseAdapter() {
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
        comboPanel.add(new JLabel("author:"));
        comboPanel.add(comboauthor);
        comboPanel.add(Box.createVerticalStrut(20));
        comboauthor.addActionListener(e -> {
            String name = (String) comboauthor.getSelectedItem();
            currentauthor = AuthorDAO.findByName(name);
            authorMode = true;
            authorPanel.setVisible(true);
            bookPanel.setVisible(false);
            fillauthorFields();
            pack();
        });
        comboPanel.add(new JLabel("book:"));
        comboPanel.add(combobook);
        comboPanel.add(Box.createVerticalStrut(20));
        combobook.addActionListener(e -> {
            String name = (String) combobook.getSelectedItem();
            currentbook = BookDAO.findByName(name);
            authorMode = false;
            authorPanel.setVisible(false);
            bookPanel.setVisible(true);
            fillbookFields();
            pack();
        });
        fillComboBoxes();
        comboPanel.setVisible(false);

        bookPanel.add(new JLabel("Name:"));
        bookPanel.add(textbookName);
        bookPanel.add(Box.createVerticalStrut(20));
        bookPanel.add(new JLabel("author Name:"));
        bookPanel.add(textbookauthorName);
        bookPanel.add(Box.createVerticalStrut(20));
        bookPanel.add(new JLabel("Release year:"));
        bookPanel.add(textbookYear);
        bookPanel.add(Box.createVerticalStrut(20));
        bookPanel.setVisible(false);

        authorPanel.add(new JLabel("Name:"));
        authorPanel.add(textauthorName);
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
                delete();
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
        btnAddauthor.setMaximumSize(dimension);
        btnAddbook.setMaximumSize(dimension);
        btnEdit.setMaximumSize(dimension);
        btnBack.setMaximumSize(dimension);
        btnSave.setMaximumSize(dimension);
        btnDelete.setMaximumSize(dimension);

        btnAddauthor.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAddbook.setAlignmentX(Component.CENTER_ALIGNMENT);
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

        comboauthor.setPreferredSize(dimension);
        combobook.setPreferredSize(dimension);

        textbookauthorName.setPreferredSize(dimension);
        textbookName.setPreferredSize(dimension);
        textbookYear.setPreferredSize(dimension);
        textauthorName.setPreferredSize(dimension);
    }

    private static void save() {
        if (editMode) {
            if (authorMode) {
                currentauthor.setName(textauthorName.getText());
                if (!AuthorDAO.update(currentauthor)) {
                    JOptionPane.showMessageDialog(null, "Error: update failed!");
                }
            } else {
                currentbook.setName(textbookName.getText());
                currentbook.setReleaseYear(Integer.parseInt(textbookYear.getText()));

                AuthorDTO author = AuthorDAO.findByName(textbookauthorName.getText());
                if (author == null) {
                    JOptionPane.showMessageDialog(null, "Error: no such country!");
                    return;
                }
                currentbook.setAuthorId(author.getId());

                if (!BookDAO.update(currentbook)) {
                    JOptionPane.showMessageDialog(null, "Error: update failed!");
                }
            }
        } else {
            if (authorMode) {
                AuthorDTO author = new AuthorDTO();
                author.setName(textauthorName.getText());
                if (!AuthorDAO.insert(author)) {
                    JOptionPane.showMessageDialog(null, "Error: insertion failed!");
                    return;
                }
                comboauthor.addItem(author.getName());
            } else {
                BookDTO book = new BookDTO();
                book.setName(textbookName.getText());
                book.setReleaseYear(Integer.parseInt(textbookYear.getText()));

                AuthorDTO author = AuthorDAO.findByName(textbookauthorName.getText());
                if (author == null) {
                    JOptionPane.showMessageDialog(null, "Error: no such author!");
                    return;
                }
                book.setAuthorId(author.getId());

                if (!BookDAO.insert(book)) {
                    JOptionPane.showMessageDialog(null, "Error: insertion failed!");
                    return;
                }
                combobook.addItem(book.getName());
            }
        }
    }

    private static void delete() {
        if (editMode) {
            if (authorMode) {
                List<BookDTO> list = BookDAO.findByauthorId(currentauthor.getId());
                assert list != null;
                for (BookDTO m : list) {
                    combobook.removeItem(m.getName());
                    BookDAO.delete(m);
                }
                AuthorDAO.delete(currentauthor);
                comboauthor.removeItem(currentauthor.getName());
            } else {
                BookDAO.delete(currentbook);
                combobook.removeItem(currentbook.getName());
            }
        }
    }

    private void fillComboBoxes() {
        comboauthor.removeAllItems();
        combobook.removeAllItems();
        List<AuthorDTO> authors = AuthorDAO.findAll();
        List<BookDTO> books = BookDAO.findAll();
        assert authors != null;
        for (AuthorDTO author : authors) {
            comboauthor.addItem(author.getName());
        }
        assert books != null;
        for (BookDTO book : books) {
            combobook.addItem(book.getName());
        }
    }

    private static void clearFields() {
        textauthorName.setText("");
        textbookName.setText("");
        textbookauthorName.setText("");
        textbookYear.setText("");
        currentauthor = null;
        currentbook = null;
    }

    private static void fillauthorFields() {
        if (currentauthor == null)
            return;
        textauthorName.setText(currentauthor.getName());
    }

    private static void fillbookFields() {
        if (currentbook == null)
            return;
        AuthorDTO author = AuthorDAO.findById(currentbook.getAuthorId());
        textbookName.setText(currentbook.getName());
        assert author != null;
        textbookauthorName.setText(author.getName());
        textbookYear.setText(String.valueOf(currentbook.getReleaseYear()));
    }

    public static void main(String[] args) {
        JFrame myWindow = new Application();
        myWindow.setVisible(true);
    }
}
