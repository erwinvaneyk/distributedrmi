package nl.erwinvaneyk.communication.rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.MessageHandler;
import nl.erwinvaneyk.communication.MessageReceivedHandler;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.core.NodeAddress;

// TODO: RMI stuff needs to be split from the messageHandler interface
public class NoBufferMessageHandler extends UnicastRemoteObject implements MessageHandler {

	private final Map<String, MessageReceivedHandler> handlers = new ConcurrentHashMap<>();

	private final NodeAddress address;

	public NoBufferMessageHandler(NodeAddress address) throws RemoteException {
		super();
		this.address = address;
	}

	@Override
	public void onMessageReceived(Message message) throws RemoteException {
		onRequestReceived(message);
	}

	@Override
	public Message onRequestReceived(Message message) throws RemoteException {
		message.setTimestampReceived();
		MessageReceivedHandler handler = getHandlerFor(message);
		if(handler != null) {
			return handler.onMessage(message);
		} else {
			throw new CommunicationException("Unknown context: " + message.getContext());
		}
	}

	@Override
	public MessageHandler bind(String key, MessageReceivedHandler handler) throws RemoteException {
		handlers.put(key, handler);
		return this;
	}

	@Override
	public MessageHandler bind(MessageReceivedHandler handler) throws RemoteException {
		handlers.put(handler.getContext(), handler);
		return this;
	}

	private MessageReceivedHandler getHandlerFor(Message message) {
		return handlers.get(message.getContext());
	}
}
