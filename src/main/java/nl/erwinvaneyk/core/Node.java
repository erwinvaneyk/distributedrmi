package nl.erwinvaneyk.core;

import nl.erwinvaneyk.communication.Connector;
import nl.erwinvaneyk.communication.MessageDistributor;
import nl.erwinvaneyk.communication.MessageFactory;

public interface Node {

	NodeState getState();

	String getType();

	void disconnect();

	Connector getConnector();

	MessageDistributor getMessageDistributor();

	MessageFactory getMessageFactory();
}
