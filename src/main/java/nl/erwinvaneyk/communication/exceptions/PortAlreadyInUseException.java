package nl.erwinvaneyk.communication.exceptions;

public class PortAlreadyInUseException extends CommunicationException {

	public PortAlreadyInUseException(String message, Exception e) {
		super(message, e);
	}
}
