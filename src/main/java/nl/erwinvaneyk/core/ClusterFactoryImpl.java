package nl.erwinvaneyk.core;

import java.net.InetAddress;
import java.net.UnknownHostException;

import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.handlers.NodeConnectHandler;
import nl.erwinvaneyk.communication.handlers.NodeInitHandler;
import nl.erwinvaneyk.communication.rmi.RMISocket;

public class ClusterFactoryImpl implements ClusterFactory {

	private final NodeFactory nodeFactory;
	private final String localIp;

	public ClusterFactoryImpl(NodeFactory nodeFactory, String localIp) {
		this.nodeFactory = nodeFactory;
		this.localIp = localIp;
	}

	public ClusterFactoryImpl(NodeFactory nodeFactory) {
		String localIp;
		this.nodeFactory = nodeFactory;
		try {
			localIp = InetAddress.getLocalHost().getHostAddress();
		}
		catch (UnknownHostException e) {
			localIp = "127.0.0.1";
		}
		this.localIp = localIp;
	}

	@Override
	public Node startCluster(int port, String clusterId) throws CommunicationException {
		NodeAddress nodeAddress = new NodeAddressImpl(NodeImpl.NODE_TYPE, 0, new Address(localIp, port));
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
		NodeAddress nodeAddress = new NodeAddressImpl(nodeFactory.getNodeType(), (Integer) response.getOrThrow("id"), new Address(localIp, port));
		Node node = nodeFactory.get(nodeAddress, (String) response.getOrThrow("clusterId"));
		// connect to other nodes
		node.getState().getConnectedNodes().addAll(
				NodeConnectHandler.discoverNetwork(node.getState().getAddress(), address));
		return node;
	}
}
