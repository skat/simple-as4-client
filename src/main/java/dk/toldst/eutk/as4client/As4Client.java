package dk.toldst.eutk.as4client;

import java.net.URL;

public class As4Client implements As4ClientInterface {

    private String fpath;
    private String username;
    private String password;
    private URL url;

    public As4Client(String Fpath, String Username, String Password, URL Url)
    {
        fpath = Fpath;
        username = Username;
        password = Password;
        url = Url;
    }


    @Override
    public void setHostName(URL url) {

    }
}
