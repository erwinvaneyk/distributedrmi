package nl.erwinvaneyk.core;

import java.rmi.RemoteException;

import lombok.Data;
import nl.erwinvaneyk.communication.Connector;
import nl.erwinvaneyk.communication.ConnectorImpl;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.rmi.NoBufferMessageHandler;
import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.MessageHandler;
import nl.erwinvaneyk.communication.handlers.NodeConnectHandler;
import nl.erwinvaneyk.communication.handlers.NodeInitHandler;

@Data
public class NodeImpl implements Node {

	public static final String NODE_TYPE = NodeImpl.class.getSimpleName();

	private final NodeState state;

	private final MessageHandler messageHandler;
	private final Server registry;
	private final Connector connector;

	protected NodeImpl(NodeAddress address, Server registry, String clusterId) throws CommunicationException {
		this.state = new NodeState(address, clusterId);
		this.registry = registry;
		this.connector = new ConnectorImpl(this.getState());
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

	@Override
	public String getType() {
		return NODE_TYPE;
	}

	@Override
	public void disconnect() {
		registry.shutdown();
	}
}
