package dk.toldst.eutk.as4client;

public interface As4Client {
    public String executePush(String service, String action, String message);
    public String executePull();
}
