package ileinterdite.util;

public class Message {

    public Message(Utils.Action action) {
        this.action = action;
        this.message = "";
        this.additionalMessage = "";
    }

    public Message(Utils.Action action, String message) {
        this.action = action;
        this.message = message;
    }

    public Utils.Action action;
    public String message;
}
