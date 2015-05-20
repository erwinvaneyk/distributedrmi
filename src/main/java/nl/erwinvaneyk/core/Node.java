package nl.erwinvaneyk.core;

import nl.erwinvaneyk.communication.Connector;
import nl.erwinvaneyk.communication.MessageDistributor;

public interface Node {

	NodeState getState();

	String getType();

	void disconnect();

	Connector getConnector();

	MessageDistributor getMessageDistributor();
}
