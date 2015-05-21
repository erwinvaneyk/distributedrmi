package nl.erwinvaneyk.communication;

import nl.erwinvaneyk.core.NodeAddress;
import nl.erwinvaneyk.core.logging.LogMessage;

public class BasicMessageFactory implements MessageFactory {

	private final NodeAddress origin;

	public BasicMessageFactory(NodeAddress origin) {
		this.origin = origin;
	}

	@Override
	public Message create(String context) {
		return new BasicMessage(context, origin);
	}

	@Override
	public Message createLog(String message) {
		return new LogMessage(message, origin);
	}
}
