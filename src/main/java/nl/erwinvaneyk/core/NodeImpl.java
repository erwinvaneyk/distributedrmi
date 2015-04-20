package nl.erwinvaneyk.core;

import java.rmi.RemoteException;

import lombok.Data;
import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.rmi.NoBufferMessageHandler;
import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.MessageHandler;
import nl.erwinvaneyk.communication.rmi.RMIRegistry;
import nl.erwinvaneyk.communication.rmi.RMISocket;

@Data
public class NodeImpl implements Node {

	private final NodeState state;

	private final MessageHandler messageHandler;
	private final Server registry;

	protected NodeImpl(NodeAddress address, Server registry, String clusterId) throws CommunicationException {
		this.state = new NodeState(address, clusterId);
		this.registry = registry;
		try {
			this.messageHandler = new NoBufferMessageHandler(address);
		}
		catch (RemoteException e) {
			throw new CommunicationException("Failed to launch MessageHandler", e);
		}
		// Setup registry
		registry.start(address.getLocation().getPort());
		registry.register(address, messageHandler);
	}

	public static Node startCluster(int port, String clusterId) throws CommunicationException {
		NodeAddress nodeAddress = new NodeAddressImpl(NodeImpl.class.getSimpleName(), 0, Address.getMyAddress(port));
		Server server = new RMIRegistry();
		return new NodeImpl(nodeAddress, server, clusterId);
	}

	public static Node connectToCluster(int port, NodeAddress address) {
		// TODO: reserve id
		Message reserveIdMessage = new BasicMessage("RESERVE_ID", null);
		RMISocket socket = new RMISocket(address);
		// TODO: acknowledge other servers
		return null;
	}

	@Override
	public String getType() {
		return this.getClass().getSimpleName();
	}

	@Override
	public void disconnect() {
		registry.shutdown();
	}
}
