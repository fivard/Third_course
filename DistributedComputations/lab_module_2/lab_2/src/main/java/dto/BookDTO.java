package dto;

public class BookDTO {
    private Long bookId;
    private Long authorId;
    private String name;
    private Integer releaseYear;

    public BookDTO(Long authorId, Long bookId, String name, Integer releaseYear) {
        this.authorId = authorId;
        this.bookId = bookId;
        this.name = name;
        this.releaseYear = releaseYear;
    }

    public BookDTO() { }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
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
}