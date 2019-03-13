package de.kneitzel.net.imap;

import javax.mail.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Scanner that watches an IMAP account.
 */
public class ImapAccountMonitor {

    // ==============================================================================================================
    // Member variables
    // ==============================================================================================================

    /**
     * Logger to use in this class.
     */
    private static Logger log = LoggerFactory.getLogger(ImapAccountMonitor.class);

    /**
     * IMAP Account to monitor.
     */
    protected ImapAccount account;

    /**
     * IMAP Session
     */
    protected Session session;

    /**
     * Imap Store
     */
    protected Store store;

    /**
     * Folders that are monitored.
     */
    protected Map<String, Folder> folders = new HashMap<String, Folder>();

    // ==============================================================================================================
    // Initialisation and close
    // ==============================================================================================================

    /**
     * Creates a new instance of ImapAccountMonitor.
     */
    public ImapAccountMonitor(ImapAccount account) throws NoSuchProviderException, MessagingException {
        log.trace("Constructor ({})", account.toString());
        this.account = account;

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", account.getProtocol());
        session = Session.getDefaultInstance(props);
        try {
            store = session.getStore("imaps");
            store.connect(account.server, account.user, account.password);
        } catch (NoSuchProviderException ex) {
            log.error("Unable to get imaps Store.", ex);
            throw ex;
        } catch (MessagingException ex) {
            log.error("Unable to connect.", ex);
            throw ex;
        }

        log.trace("Constructor done.");
    }

    /**
     * Closes all open folders and the store
     */
    public void close() {
        log.trace("close() called.");

        // Close the folders
        for (Folder folder: folders.values()) {
            try {
                folder.close(false);
            } catch (MessagingException ex) {
                // Only log the exception.
                log.warn("Exception when closing folder.", ex);
            }
        }

        // Close the store
        try {
            store.close();
        } catch (MessagingException ex) {
            // Only log the error.
            log.warn("Exception when closing the store.", ex);
        }

        log.trace("close() call ended.");
    }

    // ==============================================================================================================
    // Folder handling
    // ==============================================================================================================

    /**
     * Add Folder to monitor
     * @param name Name of the folder to monitor.
     */
    public void addFolder(String name) throws MessagingException {
        log.trace("addFolder(%s) called.", name);
        Folder folder = store.getFolder(name);
        folder.open(Folder.READ_WRITE);
        folders.put(name, folder);
        log.trace("addFolder({}) call ended.", name);
    }

    /**
     * Remove a folder from monitoring.
     * @param name name of the folde to remove from monitoring.
     */
    public void removeFolder(String name) {
        log.trace("removeFolder({}) called.", name);

        // Validate folder is known.
        if (!folders.containsKey(name)) return;

        Folder folder = folders.get(name);
        folders.remove(name);
        try {
            folder.close(false);
        } catch (MessagingException ex) {
            // TODO: Handle exception ...
        }

        log.trace("removeFolder({}) call ended.", name);
    }

    // ==============================================================================================================
    // Main routine that is doing all checks.
    // ==============================================================================================================

    /**
     * Do all checks that must be done
     */
    public void check() {
        log.trace("check() called.");

        // Loop through all folders that are monitored
        for (Folder folder: folders.values()) {
            try {
                // Loop through all messages.
                Message[] messages = folder.getMessages();
                for (Message message: messages) {
                    // Check if message wasn't seen and wasn't deleted
                    if (!message.isSet(Flags.Flag.SEEN) && !message.isSet(Flags.Flag.DELETED)) {
                        // Mark message as seen.
                        message.setFlag(Flags.Flag.SEEN, true);

                        // Raise NewMailEvent
                        raiseNewEmailEvent(message);
                    }
                }
            } catch (MessagingException ex) {
                log.error("Exception when reading messages of folder {}.", folder.getName(), ex);
                // So far no handling of this error except logging it.
            }
        }

        log.trace("check() call ended.");
    }

    // ==============================================================================================================
    // imapListener stuff
    // ==============================================================================================================

    /**
     * List of all imapListeners.
     */
    private Collection<ImapListener> imapListeners = new ArrayList<ImapListener>();

    /**
     * Add an ImapListener
     * @param listener Listener to add.
     */
    public void addImapListener(ImapListener listener) {
        log.trace("addImapListener() called!");
        imapListeners.add(listener);
    }

    /**
     * Removes an ImapListener
     * @param listener Listener to remove.
     */
    public void removeImapListener(ImapListener listener) {
        log.trace("removeImapListener() called!");
        if (imapListeners.contains(listener)) {
            log.info("Removing the IMAP listener.");
            imapListeners.remove(listener);
        }
    }

    /**
     * Raise the NewEmailEvent.
     * @param message Message that came in.
     */
    protected void raiseNewEmailEvent(Message message) {
        log.trace("raiseNewEmailEvent() called!");

        // Create the event.
        NewEmailEvent event = new NewEmailEvent(this, message);

        // Call all listeners.
        for (ImapListener listener: imapListeners) {
            log.trace("Calling listener in {} ....", listener.getClass().getName());
            listener.NewEmailReceived(event);

            // Check if the event was handled so no further ImaListeners will be called.
            if (event.getHandled() == true) {
                log.trace("raiseNewEmailEvent(): Breaking out of listener loop because event was handled.");
                break;
            }
        }

        log.trace("raiseNewEmailEvent() call ended!");
    }
}
