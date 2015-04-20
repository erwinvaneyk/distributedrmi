package nl.erwinvaneyk.communication.rmi;

import lombok.Getter;
import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.MessageReceivedHandler;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;

public class TestMessageHandler implements MessageReceivedHandler {

	public static final String CONTEXT = "RMI_TEST";

	@Getter
	private Message lastMessage;

	@Override
	public Message onMessage(Message message) throws CommunicationException {
		Message response = new BasicMessage(message.getContext(), null);
		response.put("success", true);
		lastMessage = message;
		return response;
	}

	@Override
	public String getContext() {
		return CONTEXT;
	}
}
