package demo.dto;

public class QuoteDto {
    private final String text;
    private final String author;

    public QuoteDto(String text, String author) {
        this.text = text;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }
}
