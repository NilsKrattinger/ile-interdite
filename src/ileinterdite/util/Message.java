package ileinterdite.util;

import ileinterdite.model.adventurers.Adventurer;

public class Message {

    public Utils.Action action;
    public String message;
    public Adventurer adventurer;

    public Message() {
    }

    public Message(Utils.Action action) {
        this(action, "");
    }

    public Message(Utils.Action action, String message) {
        this(action, message, null);
    }

    public Message(Utils.Action action, String message, Adventurer adventurer) {
        this.action = action;
        this.message = message;
        this.adventurer = adventurer;
    }


}
