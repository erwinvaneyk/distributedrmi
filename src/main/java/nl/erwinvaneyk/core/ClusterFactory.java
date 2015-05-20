package nl.erwinvaneyk.core;

import nl.erwinvaneyk.communication.exceptions.CommunicationException;

public interface ClusterFactory {

	Node startCluster(int port, String clusterId) throws CommunicationException;

	Node connectToCluster(int port, NodeAddress address) throws CommunicationException;

	public static ClusterFactory getBasicFactory() {
		return new ClusterFactoryImpl(new NodeFactoryImpl());
	}
}
