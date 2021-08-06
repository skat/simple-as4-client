package dk.toldst.eutk.as4client.userinformation;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. Please write your own text here.
 * User: brj@netcompany.com - cbm@netcompany.com
 * Date: 2017-01-10 - 2021-08-05
 */
public class As4UserInformation implements Serializable {
    private static final long serialVersionUID = 1;
    private final As4UserInformationType type;

    private String cvr = null;
    private String id;

    public As4UserInformation(final As4UserInformationType type, final String id, final String cvrOptional) {
        this.type = type;
        this.id = id;
        if (cvrOptional != null) {
            cvr = cvrOptional;
        }
    }

    public As4UserInformationType getType() {
        return type;
    }

    public String getCvr() {
        return cvr;
    }

    public void setCvr(String cvr) {
        this.cvr = cvr;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}