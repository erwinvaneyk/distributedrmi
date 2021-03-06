package nl.erwinvaneyk.core.logging;

import java.rmi.RemoteException;

import lombok.Setter;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.MessageReceivedHandler;
import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.core.NodeAddress;
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

	@Override
	public String getType() {
		return NODE_TYPE;
	}
}
