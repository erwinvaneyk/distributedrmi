package nl.erwinvaneyk.communication;

import java.util.List;
import java.util.Set;

import lombok.NonNull;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.core.NodeAddress;

public interface Connector {

	void sendMessage(Message message, NodeAddress destination) throws CommunicationException;

	Message sendRequest(Message message, NodeAddress destination) throws CommunicationException;

	Set<NodeAddress> broadcast(Message message, String... identifierFilter);
}
