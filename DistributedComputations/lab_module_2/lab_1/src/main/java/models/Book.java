package models;


public class Book {
    private String bookId;
    private String authorId;
    private String name;
    private Integer releaseYear;

    public Book(String name, Integer releaseYear, Library library) {
        this.name = name;
        this.releaseYear = releaseYear;
        library.createId(this);
    }

    public Book() {
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public void outputBook(){
        System.out.println("\tid=" + bookId + "\tname=" + name + "\treleaseYear=" + releaseYear);
    }
}