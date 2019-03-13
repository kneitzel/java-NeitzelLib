package de.kneitzel.net.imap;

import java.util.Objects;

/**
 * Information about an ImapAcocunt
 */
public class ImapAccount {

    /**
     * Server to connect to.
     */
    protected String server;
    public String getServer() { return server; }
    public void setServer(String server) { this.server = server; }

    /**
     * User to use for authentication
     */
    protected String user;
    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    /**
     * Password to use for authentication
     */
    protected String password;
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    /**
     * Protocol to use.
     */
    protected String protocol;
    public String getProtocol() { return protocol; }
    public void setProtocol(String protocol) { this.protocol = protocol; }

    /**
     * Prints the ImapAccount details.
     * @return The ImapAccount details without the password.
     */
    @Override public String toString() {
        return String.format("ImapAccount(%s, %s, %s, %s",
                server,
                user,
                password.length()==0 ? "''" : "########" ,
                protocol);
    }

    /**
     * Returns the hashcode of the imap account details.
     * @return Hashcode of the instance.
     */
    @Override public int hashCode() {
        return Objects.hash(server, user, password, protocol);
    }

    @Override public boolean equals(Object obj) {
        // Check type of argument, includes check of null.
        if (!(obj instanceof ImapAccount)) return false;

        // Check Reference equals
        if (this == obj) return true;

        // Check the comparison of all field
        ImapAccount other = (ImapAccount) obj;
        return (server == other.server || (server != null && server.equals(other.server))) &&
                (user == other.user || (user != null && user.equals(other.user))) &&
                (password == other.password || (password != null && password.equals(other.password))) &&
                (protocol == other.protocol || (protocol != null && protocol.equals(other.protocol)));
    }
}
