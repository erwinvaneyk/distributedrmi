package nl.erwinvaneyk.core.logging;

import java.rmi.RemoteException;

import lombok.Setter;
import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.MessageReceivedHandler;
import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.handlers.NodeConnectHandler;
import nl.erwinvaneyk.communication.handlers.NodeInitHandler;
import nl.erwinvaneyk.communication.rmi.RMIRegistry;
import nl.erwinvaneyk.communication.rmi.RMISocket;
import nl.erwinvaneyk.core.Address;
import nl.erwinvaneyk.core.NodeAddress;
import nl.erwinvaneyk.core.NodeAddressImpl;
import nl.erwinvaneyk.core.NodeImpl;

public class LogNode extends NodeImpl {

	public static final String CONTEXT = "log";
	public static final String NODE_TYPE = "LOGGER";

	@Setter
	private Logger logger = new PrintLogger();

	protected LogNode(NodeAddress address, Server registry,
			String clusterId) throws CommunicationException {
		super(address, registry, clusterId);
		// Bind logger
		try {
			this.getMessageDistributor().bind(new MessageReceivedHandler() {
				@Override
				public Message onMessage(Message message) throws CommunicationException {
					logger.log(message);
					return null;
				}

				@Override
				public String getContext() {
					return CONTEXT;
				}
			});
		}
		catch (RemoteException e) {
			throw new CommunicationException("Failed to launch MessageHandler for logger", e);
		}
	}

	public static LogNode connectToCluster(int port, NodeAddress address) throws CommunicationException {
		// TODO: reserve id
		// Request ID from a cluster-node
		Message reserveIdMessage = new BasicMessage(NodeInitHandler.NODE_CONNECT, null).put("type", NODE_TYPE);
		RMISocket socket = new RMISocket(address);
		Message response = socket.sendRequest(reserveIdMessage);
		// Create node based on response
		NodeAddress nodeAddress = new NodeAddressImpl(NODE_TYPE, (Integer) response.getOrThrow("id"), Address.getMyAddress(
				port));
		LogNode node = new LogNode(nodeAddress, new RMIRegistry(),(String) response.getOrThrow("clusterId"));
		// connect to other nodes
		node.getState().getConnectedNodes()
				.addAll(NodeConnectHandler.discoverNetwork(node.getState().getAddress(), address));
		return node;
	}

	@Override
	public String getType() {
		return NODE_TYPE;
	}
}
