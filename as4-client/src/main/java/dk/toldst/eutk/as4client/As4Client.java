package dk.toldst.eutk.as4client;

import javax.xml.transform.TransformerException;
import java.io.IOException;

public interface As4Client {
    String executePush(String service, String action, String message) throws IOException, TransformerException;
    String executePush(String service, String action, byte[] message) throws IOException, TransformerException;
    String executePull();
}
