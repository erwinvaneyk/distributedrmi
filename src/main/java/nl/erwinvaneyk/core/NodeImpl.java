package nl.erwinvaneyk.core;

import java.rmi.RemoteException;

import lombok.Data;
import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.communication.Connector;
import nl.erwinvaneyk.communication.ConnectorImpl;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.rmi.NoBufferMessageHandler;
import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.MessageHandler;
import nl.erwinvaneyk.communication.rmi.RMIRegistry;
import nl.erwinvaneyk.communication.rmi.RMISocket;
import nl.erwinvaneyk.communication.handlers.NodeConnectHandler;
import nl.erwinvaneyk.communication.handlers.NodeInitHandler;

@Data
public class NodeImpl implements Node {

	private static final String NODE_TYPE = NodeImpl.class.getSimpleName();

	private final NodeState state;

	private final MessageHandler messageHandler;
	private final Server registry;
	private final Connector connector;

	protected NodeImpl(NodeAddress address, Server registry, String clusterId) throws CommunicationException {
		this.state = new NodeState(address, clusterId);
		this.registry = registry;
		this.connector = new ConnectorImpl(this);
		try {
			messageHandler = new NoBufferMessageHandler(address);
			messageHandler.bind(new NodeInitHandler(connector, state));
			messageHandler.bind(new NodeConnectHandler(state));
		}
		catch (RemoteException e) {
			throw new CommunicationException("Failed to launch MessageHandler", e);
		}
		// Setup registry
		registry.start(address.getLocation().getPort());
		registry.register(address, messageHandler);
	}

	public static Node startCluster(int port, String clusterId) throws CommunicationException {
		NodeAddress nodeAddress = new NodeAddressImpl(NODE_TYPE, 0, Address.getMyAddress(port));
		Server server = new RMIRegistry();
		return new NodeImpl(nodeAddress, server, clusterId);
	}

	public static Node connectToCluster(int port, NodeAddress address) throws CommunicationException {
		// TODO: reserve id
		// Request ID from a cluster-node
		Message reserveIdMessage = new BasicMessage(NodeInitHandler.NODE_CONNECT, null).put("type", NODE_TYPE);
		RMISocket socket = new RMISocket(address);
		Message response = socket.sendRequest(reserveIdMessage);
		// Create node based on response
		NodeAddress nodeAddress = new NodeAddressImpl(NODE_TYPE, (Integer) response.getOrThrow("id"), Address.getMyAddress(port));
		Node node = new NodeImpl(nodeAddress, new RMIRegistry(),(String) response.getOrThrow("clusterId"));
		// connect to other nodes
		node.getState().getConnectedNodes()
				.addAll(NodeConnectHandler.discoverNetwork(node.getState().getAddress(), address));
		return node;
	}

	@Override
	public String getType() {
		return NODE_TYPE;
	}

	@Override
	public void disconnect() {
		registry.shutdown();
	}
}
