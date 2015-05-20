package nl.erwinvaneyk.communication;

import nl.erwinvaneyk.core.NodeAddress;
import nl.erwinvaneyk.core.logging.LogMessage;

public class MessageFactory {

	private final NodeAddress origin;

	public MessageFactory(NodeAddress origin) {
		this.origin = origin;
	}

	public Message create(String context) {
		return new BasicMessage(context, origin);
	}

	public Message createLog(String message) {
		return new LogMessage(message, origin);
	}
}
