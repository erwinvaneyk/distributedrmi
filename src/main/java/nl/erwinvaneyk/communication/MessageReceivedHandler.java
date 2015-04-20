package nl.erwinvaneyk.communication;


import nl.erwinvaneyk.communication.exceptions.CommunicationException;

public interface MessageReceivedHandler {

	Message onMessage(Message message) throws CommunicationException;

	String getContext();
}
