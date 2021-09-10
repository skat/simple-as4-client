package dk.toldst.eutk.as4client.builder.interfaces;

import dk.toldst.eutk.as4client.builder.As4ClientBuilder;

public interface As4SetPasswordTokenDetails
{
    /**
     * Sets the password and returns the final AS4ClientBuilder which can then be built.
     * @param password for the Crypto.
     * @return
     */
    As4ClientBuilder setPassword(String password);
}
