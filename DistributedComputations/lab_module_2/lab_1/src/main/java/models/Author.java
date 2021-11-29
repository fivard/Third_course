package models;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private String authorId;
    private String name;
    private List<Book> books = new ArrayList<>();

    public Author(){ }

    public Author(String name, Library library) {
        this.name = name;
        library.createId(this);
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void outputAuthor(){
        System.out.println("id=" + authorId + "\tname=" + name);
    }
}