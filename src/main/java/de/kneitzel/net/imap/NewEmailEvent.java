package de.kneitzel.net.imap;

import javax.mail.Message;
import java.util.EventObject;

/**
 * New Email arrived Event.
 */
public class NewEmailEvent extends EventObject {

    /**
     * Message that was received.
     */
    protected Message message;
    public Message getMessage() { return message; }
    public void setMessage(Message message) { this.message = message; }

    /**
     * Flag that marks the event has handled.
     */
    protected boolean handled = false;
    public boolean getHandled() { return handled; }
    public void setHandled(boolean handled) { this.handled = handled; }

    /**
     * Creates a new instance of NewEmailEvent.
     * @param source Source of the event.
     */
    public NewEmailEvent(Object source, Message message) {
        super(source);
        this.message = message;
    }
}
