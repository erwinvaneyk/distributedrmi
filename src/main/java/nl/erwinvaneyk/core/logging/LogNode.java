package nl.erwinvaneyk.core.logging;

import java.rmi.RemoteException;

import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.MessageReceivedHandler;
import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.communication.rmi.RMISocket;
import nl.erwinvaneyk.core.Node;
import nl.erwinvaneyk.core.NodeAddress;
import nl.erwinvaneyk.core.NodeImpl;

public class LogNode extends NodeImpl {

	public static final String CONTEXT = "log";
	public static final String NODETYPE = "LOGGER";

	private Logger logger;

	protected LogNode(NodeAddress address, Server registry,
			String clusterId) throws RemoteException {
		super(address, registry, clusterId);
		// Bind logger
		this.getMessageHandler().bind(new MessageReceivedHandler() {
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

	public static Node connectToCluster(int port, NodeAddress address, Logger logger) {
		// TODO: reserve id
		Message reserveIdMessage = new BasicMessage("RESERVE_ID", null);
		RMISocket socket = new RMISocket(address);
		// TODO: acknowledge other servers
		return null;
	}

	@Override
	public String getType() {
		return NODETYPE;
	}
}
