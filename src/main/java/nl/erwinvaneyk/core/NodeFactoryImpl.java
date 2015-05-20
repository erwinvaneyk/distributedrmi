package nl.erwinvaneyk.core;

import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.rmi.RMIRegistry;

public class NodeFactoryImpl implements NodeFactory {

	@Override
	public Node get(NodeAddress nodeAddress, String clusterId) throws CommunicationException {
		Server server = new RMIRegistry();
		return new NodeImpl(nodeAddress, server, clusterId);
	}
}
