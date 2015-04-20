package nl.erwinvaneyk.core;

import nl.erwinvaneyk.communication.MessageHandler;

public interface Node {

	public NodeState getState();

	public String getType();

	public void disconnect();

	MessageHandler getMessageHandler();
}
