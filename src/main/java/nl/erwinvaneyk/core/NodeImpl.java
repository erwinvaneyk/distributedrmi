package nl.erwinvaneyk.core;

import java.rmi.RemoteException;

import lombok.Getter;
import lombok.ToString;
import nl.erwinvaneyk.communication.BasicMessageFactory;
import nl.erwinvaneyk.communication.Connector;
import nl.erwinvaneyk.communication.ConnectorImpl;
import nl.erwinvaneyk.communication.MessageDistributor;
import nl.erwinvaneyk.communication.MessageFactory;
import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.handlers.NodeConnectHandler;
import nl.erwinvaneyk.communication.handlers.NodeInitHandler;
import nl.erwinvaneyk.communication.rmi.NoBufferMessageHandler;

@ToString
public class NodeImpl implements Node {

	public static final String NODE_TYPE = NodeImpl.class.getSimpleName();

	@Getter
	private final NodeState state;
	@Getter
	private final MessageDistributor messageDistributor;
	@Getter
	private final Connector connector;
	@Getter
	private final MessageFactory messageFactory;

	private final Server server;

	protected NodeImpl(NodeAddress address, Server server, String clusterId) throws CommunicationException {
		this.state = new NodeState(address, clusterId);
		this.server = server;
		this.connector = new ConnectorImpl(this.getState());
		this.messageFactory = new BasicMessageFactory(address);
		try {
			messageDistributor = new NoBufferMessageHandler(address);
			messageDistributor.bind(new NodeInitHandler(connector, state));
			messageDistributor.bind(new NodeConnectHandler(state));
		}
		catch (RemoteException e) {
			throw new CommunicationException("Failed to launch MessageHandler", e);
		}
		// Setup server
		server.start(address.getLocation().getPort());
		server.register(address, messageDistributor);
	}

	@Override
	public String getType() {
		return NODE_TYPE;
	}

	@Override
	public void disconnect() {
		server.shutdown();
	}
}
