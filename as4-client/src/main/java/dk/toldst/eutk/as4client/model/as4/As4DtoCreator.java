package dk.toldst.eutk.as4client.model.as4;

import dk.toldst.eutk.as4client.As4ClientInstance;
import dk.toldst.eutk.as4client.model.as4.As4Message.As4Part;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.CollaborationInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.From;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.MessageProperties;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartProperties;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyId;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PartyInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PayloadInfo;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Property;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.PullRequest;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.SignalMessage;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.To;
import org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.UserMessage;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;


public class As4DtoCreator {

    private final As4Properties as4Properties;
    private final As4ClientInstance clientInstance;

    public As4DtoCreator(As4Properties as4Properties, As4ClientInstance clientInstance) {
        this.as4Properties = as4Properties;
        this.clientInstance = clientInstance;

    }


    public UserMessage createUserMessaging(String service, String action, String conversationId, As4Message payload,
                                           String messageId) {
        MessageInfo messageInfo = createMessageInfo(messageId);
        PartyInfo partyInfo = createPartyInfo();
        CollaborationInfo collaborationInfo = createCollaborationInfo(service, action, conversationId);
        MessageProperties messageProperties = createMessageProperties(payload.getMessageProperties());
        PayloadInfo payloadInfo = createPayloadInfo(payload, messageId);

        UserMessage userMessage = new UserMessage();
        userMessage.setMessageInfo(messageInfo);
        userMessage.setCollaborationInfo(collaborationInfo);
        userMessage.setPartyInfo(partyInfo);
        userMessage.setMessageProperties(messageProperties);
        userMessage.setPayloadInfo(payloadInfo);

        return userMessage;
    }

    private MessageProperties createMessageProperties(Map<String, String> messageProperties) {
        if (messageProperties == null) {
            return null;
        }
        MessageProperties messagePropertiesElement = new MessageProperties();
        messageProperties.forEach((k, v) -> {
            Property property = new Property();
            property.setValue(v);
            property.setName(k);
            messagePropertiesElement.getProperty().add(property);
        });

        return messagePropertiesElement;
    }

    private MessageInfo createMessageInfo(String messageId) {
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setMessageId(messageId + "@" + as4Properties.getLocalHostName());
        LocalDateTime date =  LocalDateTime.now();
        messageInfo.setTimestamp(date);
        return messageInfo;
    }

    private PayloadInfo createPayloadInfo(As4Message as4Message, String messageId) {
        PayloadInfo payloadInfo = new PayloadInfo();

        if (as4Message.getBody() != null) {
            PartInfo partInfo = new PartInfo();
            partInfo.setPartProperties(createPartProperties(as4Message.getBody().getProperties()));
            payloadInfo.getPartInfo().add(partInfo);
        }

        for (As4Part as4Part : as4Message.getAttachments()) {
            PartInfo partInfo = new PartInfo();
            partInfo.setPartProperties(createPartProperties(as4Part.getProperties()));

            String id = createId(messageId);
            as4Part.setId(id);
            partInfo.setHref("cid:" + id);
            payloadInfo.getPartInfo().add(partInfo);
        }

        return payloadInfo;
    }

    private PartProperties createPartProperties(Map<String, String> properties) {
        if (properties == null) {
            return null;
        }

        PartProperties partProperties = new PartProperties();
        properties.forEach((k, v) -> {
            Property property = new Property();
            property.setName(k);
            property.setValue(v);
            partProperties.getProperty().add(property);
        });

        return partProperties;
    }

    private CollaborationInfo createCollaborationInfo(String service, String action, String conversationId) {
        org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Service serviceElement =
                new org.oasis_open.docs.ebxml_msg.ebms.v3_0.ns.core._200704.Service();
        serviceElement.setType("string");
        serviceElement.setValue(service);

        CollaborationInfo collaborationInfo = new CollaborationInfo();
        collaborationInfo.setAction(action);
        collaborationInfo.setService(serviceElement);
        collaborationInfo.setConversationId(conversationId);

        return collaborationInfo;
    }

    private PartyInfo createPartyInfo() {
        PartyId fromParty = new PartyId();
        fromParty.setType("string");
        fromParty.setValue(as4Properties.getFrom().getId());

        PartyId toParty = new PartyId();
        toParty.setType("string");
        toParty.setValue(as4Properties.getTo().getId());

        From from = new From();
        from.setRole(as4Properties.getFrom().getRole());
        from.getPartyId().add(fromParty);

        To to = new To();
        to.setRole(as4Properties.getTo().getRole());
        to.getPartyId().add(toParty);

        PartyInfo partyInfo = new PartyInfo();
        partyInfo.setFrom(from);
        partyInfo.setTo(to);
        return partyInfo;
    }

    private String createId(String messageId) {
        return String.format("%s-%s@%s", messageId, UUID.randomUUID(),
                as4Properties.getLocalHostName());
    }

    public SignalMessage createPullRequest(String mpc, String messageId) {
        PullRequest pullRequest = new PullRequest();
        pullRequest.setMpc(mpc);

        SignalMessage signalMessage = new SignalMessage();
        signalMessage.setMessageInfo(createMessageInfo(messageId));
        signalMessage.setPullRequest(pullRequest);
        return signalMessage;
    }
}