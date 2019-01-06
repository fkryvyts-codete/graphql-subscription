package demo.dto;

public class MailMessageDto {
    private String text;

    public MailMessageDto(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
