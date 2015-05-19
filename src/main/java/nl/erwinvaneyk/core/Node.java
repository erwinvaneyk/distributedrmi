package nl.erwinvaneyk.core;

import nl.erwinvaneyk.communication.MessageHandler;

public interface Node {

	NodeState getState();

	String getType();

	void disconnect();

	MessageHandler getMessageHandler();
}
