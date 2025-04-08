package hcmute.edu.vn.selfalarm.model;

public class SMS {
    private String sender;
    private String message;

    public SMS(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}