package dk.toldst.eutk.as4client.as4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class As4Message {
    private List<As4Part> attachments = new ArrayList<>();
    private As4Part body;
    private Map<String, String> messageProperties;

    public As4Part getBody() {
        return body;
    }

    public void setBody(As4Part body) {
        this.body = body;
    }

    public Map<String, String> getMessageProperties() {
        return messageProperties;
    }

    public void setMessageProperties(Map<String, String> messageProperties) {
        this.messageProperties = messageProperties;
    }

    public List<As4Part> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<As4Part> attachments) {
        this.attachments = attachments;
    }

    public static class As4Part {
        private String content;
        private Map<String, String> properties;

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }
    }
}
