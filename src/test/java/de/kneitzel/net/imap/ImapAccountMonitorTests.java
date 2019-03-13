package de.kneitzel.net.imap;

import de.kneitzel.TestBase;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import java.util.Properties;

public class ImapAccountMonitorTests extends TestBase implements ImapListener {

    /**
     * Logger to use in this class.
     */
    private static Logger log = LoggerFactory.getLogger(ImapAccountMonitorTests.class);

    /**
     * Counter of emails handled.
     */
    private int emailsHandledCounter;

    /**
     * Number of emails inside folder.
     */
    private int numberOfEmails;

    /**
     * Eventlistener to the
     * @param event Details about the new Email.
     */
    @Override
    public void NewEmailReceived(NewEmailEvent event) {
        emailsHandledCounter++;
    }

    @Test
    public void imapAccountMonitorTest() {
        // reset counter
        emailsHandledCounter = 0;

        // Set account details
        ImapAccount account = new ImapAccount();
        account.setServer(prop.getProperty("tests.imap.account.server"));
        account.setUser(prop.getProperty("tests.imap.account.user"));
        account.setPassword(prop.getProperty("tests.imap.account.password"));
        account.setProtocol(prop.getProperty("tests.imap.account.protocol"));

        // Mark all messages as unread and set numberOfEmails
        prepareTest(account);
        Assert.assertNotEquals(0, numberOfEmails);

        try {
            // Create ImapAccountMonitor
            ImapAccountMonitor monitor = new ImapAccountMonitor(account);

            // Add event listener
            monitor.addImapListener(this);

            // Add folder to monitor.
            monitor.addFolder(prop.getProperty("tests.imap.account.folder"));

            // Do the checks
            monitor.check();

            // Verify that everything is correct.
            Assert.assertEquals(numberOfEmails, emailsHandledCounter);

            // Do the checks again
            emailsHandledCounter = 0;
            monitor.check();
            Assert.assertEquals(0, emailsHandledCounter);

        } catch (NoSuchProviderException ex) {
            Assert.fail();
        } catch (MessagingException ex) {
            Assert.fail();
        }
    }

    /**
     * Prepares the test and makes sure that at least one message is marked as not seen!
     */
    public void prepareTest(ImapAccount account) {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", account.getProtocol());
        Session session = Session.getDefaultInstance(props);
        try {
            Store store = session.getStore("imaps");
            store.connect(account.server, account.user, account.password);
            Folder folder = store.getFolder(prop.getProperty("tests.imap.account.folder"));
            folder.open(Folder.READ_WRITE);
            Message[] messages =folder.getMessages();
            for (Message msg: messages) {
                msg.setFlag(Flags.Flag.SEEN, false);
            }
            numberOfEmails = messages.length;
            folder.close();
        } catch (NoSuchProviderException ex) {
            log.error("Unable to get imaps Store.", ex);
            Assert.fail("Unable to get imaps Store.");
        } catch (MessagingException ex) {
            log.error("Unable to connect.", ex);
            Assert.fail("Unable to connect.");
        }

    }
}
