package nl.erwinvaneyk.communication;

public interface MessageFactory {
	Message create(String context);

	Message createLog(String message);
}
