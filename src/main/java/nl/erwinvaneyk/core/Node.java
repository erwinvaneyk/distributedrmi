package nl.erwinvaneyk.core;

import nl.erwinvaneyk.communication.Connector;
import nl.erwinvaneyk.communication.MessageHandler;

public interface Node {

	NodeState getState();

	String getType();

	void disconnect();

	Connector getConnector();

	MessageHandler getMessageHandler();
}
