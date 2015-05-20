package nl.erwinvaneyk.core.logging;

import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.rmi.RMIRegistry;
import nl.erwinvaneyk.core.Node;
import nl.erwinvaneyk.core.NodeAddress;
import nl.erwinvaneyk.core.NodeFactory;

public class LogNodeFactory implements NodeFactory {

	@Override
	public Node get(NodeAddress nodeAddress, String clusterId) throws CommunicationException {
		return new LogNode(nodeAddress, new RMIRegistry(), clusterId);
	}
}
