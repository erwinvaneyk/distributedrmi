package nl.erwinvaneyk.core;

import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.handlers.NodeConnectHandler;
import nl.erwinvaneyk.communication.handlers.NodeInitHandler;
import nl.erwinvaneyk.communication.rmi.RMISocket;

public class ClusterFactoryImpl implements ClusterFactory {

	private final NodeFactory nodeFactory;

	public ClusterFactoryImpl(NodeFactory nodeFactory) {
		this.nodeFactory = nodeFactory;
	}

	@Override
	public Node startCluster(int port, String clusterId) throws CommunicationException {
		NodeAddress nodeAddress = new NodeAddressImpl(NodeImpl.NODE_TYPE, 0, Address.getMyAddress(port));
		return nodeFactory.get(nodeAddress, clusterId);
	}

	@Override
	public Node connectToCluster(int port, NodeAddress address) throws CommunicationException {
		// TODO: reserve id
		// Request ID from a cluster-node
		Message reserveIdMessage = new BasicMessage(NodeInitHandler.NODE_CONNECT, null).put("type", nodeFactory.getNodeType());
		RMISocket socket = new RMISocket(address);
		Message response = socket.sendRequest(reserveIdMessage);
		// Create node based on response
		NodeAddress nodeAddress = new NodeAddressImpl(nodeFactory.getNodeType(), (Integer) response.getOrThrow("id"), Address.getMyAddress(port));
		Node node = nodeFactory.get(nodeAddress, (String) response.getOrThrow("clusterId"));
		// connect to other nodes
		node.getState().getConnectedNodes().addAll(
				NodeConnectHandler.discoverNetwork(node.getState().getAddress(), address));
		return node;
	}
}
