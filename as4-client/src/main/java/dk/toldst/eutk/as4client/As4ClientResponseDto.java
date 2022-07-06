package dk.toldst.eutk.as4client;

public class As4ClientResponseDto {
    private String FirstAttachment;
    private String reftoOriginalID;


    public String getFirstAttachment() {
        return FirstAttachment;
    }

    public void setFirstAttachment(String firstAttachment) {
        FirstAttachment = firstAttachment;
    }

    public String getReftoOriginalID() {
        return reftoOriginalID;
    }

    public void setReftoOriginalID(String reftoOriginalID) {
        this.reftoOriginalID = reftoOriginalID;
    }
}
