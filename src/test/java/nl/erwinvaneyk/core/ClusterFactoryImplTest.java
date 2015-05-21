package nl.erwinvaneyk.core;

import static org.junit.Assert.assertEquals;

import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClusterFactoryImplTest {

	private static ClusterFactory clusterFactory;

	@BeforeClass
	public static void init() {
		clusterFactory = ClusterFactory.getBasicFactory();
	}

	@Test
	public void startCluster() throws CommunicationException {
		Node headnode = clusterFactory.startCluster(1817, "test-cluster");
		assertEquals(headnode.getState().getAddress().getLocation().getPort(), 1817);
		assertEquals(headnode.getState().getAddress().getIdentifier(), "NodeImpl-0");
		assertEquals(headnode.getState().getClusterId(), "test-cluster");
		headnode.disconnect();
	}

	@Test
	public void shutdownNode() throws CommunicationException {
		Node node = clusterFactory.startCluster(1818, "test-cluster");
		node.disconnect();
		// Check if port has been released
		clusterFactory.startCluster(1818, "test-cluster").disconnect();
	}

	@Test
	public void joinCluster() throws CommunicationException {
		Node node1 = clusterFactory.startCluster(1819, "test-cluster");
		Node node2 = clusterFactory.connectToCluster(1820, node1.getState().getAddress());
		assertEquals(node2.getState().getClusterId(), "test-cluster");
		node1.disconnect();
		node2.disconnect();
	}

	@Test
	public void joinClusterOnNonStartingNode() throws CommunicationException {
		Node node1 = clusterFactory.startCluster(1819, "test-cluster");
		Node node2 = clusterFactory.connectToCluster(1820, node1.getState().getAddress());
		Node node3 = clusterFactory.connectToCluster(1821, node2.getState().getAddress());
		Node node4 = clusterFactory.connectToCluster(1822, node3.getState().getAddress());
		assertEquals(node4.getState().getClusterId(), "test-cluster");
		assertEquals(3, node4.getState().getConnectedNodes().size());
		node1.disconnect();
		node2.disconnect();
		node3.disconnect();
		node4.disconnect();
	}
}
