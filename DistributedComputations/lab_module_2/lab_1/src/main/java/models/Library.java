package models;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Library {
    private Map<String, Author> authors = new HashMap<>();
    private Map<String, String> authorNames = new HashMap<>();
    private Map<String, Book> books = new HashMap<>();
    private Map<String, String> bookNames = new HashMap<>();

    public Library() { }

    public void createId(Author author) {
        int id = authors.size();
        String idToString = "author" + id;
        while(authors.get(idToString) != null) {
            id++;
            idToString = "author" + id;
        }
        author.setAuthorId(idToString);
    }

    public void createId(Book book) {
        int id = books.size();
        String idToString = "id" + id;
        while(authors.get(idToString) != null) {
            id++;
            idToString = "id" + id;
        }
        book.setBookId(idToString);
    }

    public void addAuthor(Author author) {
        authors.put(author.getAuthorId(), author);
        authorNames.put(author.getName(), author.getAuthorId());
    }

    public void addBook(Book book, String authorId) {
        Author author = authors.get(authorId);
        book.setAuthorId(authorId);
        books.put(book.getBookId(), book);
        bookNames.put(book.getName(), book.getBookId());
        author.getBooks().add(book);
    }

    public void removeAuthor(Author author) {
        authors.remove(author.getAuthorId());
        authorNames.remove(author.getName());
        for(Book book: author.getBooks()) {
            books.remove(book.getBookId());
        }
    }

    public void removeBook(Book book) {
        books.remove(book.getBookId());
        bookNames.remove(book.getName());
        authors.get(book.getAuthorId()).getBooks().remove(book);
    }

    public void changeBooksAuthor(Book book, Author author) {
        Author old = authors.get(book.getAuthorId());
        if(old != null) {
            old.getBooks().remove(book);
        }
        book.setAuthorId(author.getAuthorId());
        author.getBooks().add(book);
    }

    public void renameBook(Book book, String newName) {
        bookNames.remove(book.getName());
        book.setName(newName);
        bookNames.put(book.getName(), book.getBookId());
    }

    public void renameAuthor(Author author, String newName) {
        authorNames.remove(author.getName());
        author.setName(newName);
        authorNames.put(author.getName(), author.getAuthorId());
    }

    public Book getBook(String name) {
        String id = bookNames.get(name);
        if(id != null) {
            return books.get(id);
        }
        return null;
    }

    public Author getAuthor(String name) {
        String id = authorNames.get(name);
        if(id != null) {
            return authors.get(id);
        }
        return null;
    }

    public List<Book> getAuthorsBooks(String name){
        return getAuthor(name).getBooks();
    }

    public Map<String, Author> getAuthors() {
        return authors;
    }

    public void outputAllContents(){
        for (String id : authors.keySet()){
            Author author = authors.get(id);
            author.outputAuthor();
            List<Book> books = author.getBooks();
            for (Book book : books) book.outputBook();
        }
    }
}