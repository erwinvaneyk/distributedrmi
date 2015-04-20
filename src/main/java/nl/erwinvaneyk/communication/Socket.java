package nl.erwinvaneyk.communication;

import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.core.NodeAddress;

public interface Socket {

	NodeAddress getConnectedNode();

	void sendMessage(Message message) throws CommunicationException;

	Message sendRequest(Message message) throws CommunicationException;

}
