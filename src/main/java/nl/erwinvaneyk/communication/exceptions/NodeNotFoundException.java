package nl.erwinvaneyk.communication.exceptions;

public class NodeNotFoundException extends CommunicationException {

	public NodeNotFoundException(String message, Exception e) {
		super(message, e);
	}
}
