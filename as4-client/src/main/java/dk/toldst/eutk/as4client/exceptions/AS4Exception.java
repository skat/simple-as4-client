package dk.toldst.eutk.as4client.exceptions;

/**
 * Created by IntelliJ IDEA. Please write your own text here.
 * User: brj@netcompany.com
 * Date: 2017-01-05
 */
public class AS4Exception extends Exception {
    public AS4Exception(String s) {
        super(s);
    }

    public AS4Exception(String s, Exception e) {
        super(s,e);
    }
}
