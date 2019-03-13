package de.kneitzel.net.imap;

/**
 * Listener to IMAP events.
 */
public interface ImapListener {
    /**
     * New Email was received.
     * @param event Details about the new Email.
     */
    void NewEmailReceived(NewEmailEvent event);
}
