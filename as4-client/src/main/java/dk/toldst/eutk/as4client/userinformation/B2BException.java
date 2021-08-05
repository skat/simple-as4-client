package dk.toldst.eutk.as4client.userinformation;

/**
 * Created by IntelliJ IDEA. Please write your own text here.
 * User: brj@netcompany.com
 * Date: 2017-01-05
 */
public class B2BException extends Exception {
    public B2BException(String s) {
        super(s);
    }

    public B2BException(String s, Exception e) {
        super(s,e);
    }
}
