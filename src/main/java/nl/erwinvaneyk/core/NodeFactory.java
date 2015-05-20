package nl.erwinvaneyk.core;

import nl.erwinvaneyk.communication.exceptions.CommunicationException;

public interface NodeFactory {

	Node get(NodeAddress nodeAddress, String clusterId) throws CommunicationException;

	String getNodeType();
}
