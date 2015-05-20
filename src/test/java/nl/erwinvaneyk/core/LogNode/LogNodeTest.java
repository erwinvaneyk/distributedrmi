package nl.erwinvaneyk.core.LogNode;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;

import nl.erwinvaneyk.communication.ConnectorImpl;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.core.ClusterFactory;
import nl.erwinvaneyk.core.Node;
import nl.erwinvaneyk.core.NodeImpl;
import nl.erwinvaneyk.core.logging.LogMessage;
import nl.erwinvaneyk.core.logging.LogNode;
import org.junit.Test;

public class LogNodeTest {


	private Message lastLogMessage1;
	private Message lastLogMessage2;

	@Test
	public void addLogNodeToClusterAndLog() throws RemoteException {
		// Setup cluster
		ClusterFactory clusterFactory = ClusterFactory.getBasicFactory();
		Node node1 = clusterFactory.startCluster(1819, "test-cluster");
		Node node2 = clusterFactory.connectToCluster(1820, node1.getState().getAddress());
		LogNode logNode1 = LogNode.connectToCluster(1821, node1.getState().getAddress());
		assertEquals("test-cluster", logNode1.getState().getClusterId());
		// Use stub logger
		logNode1.setLogger(message -> lastLogMessage1 = message);
		// Log and check
		Message message = new LogMessage("Test message", null);
		logNode1.getMessageHandler().onMessageReceived(message);
		assertEquals(message, lastLogMessage1);
		// Shutdown
		node1.disconnect();
		node2.disconnect();
		logNode1.disconnect();
	}

	@Test
	public void logToMultipleLogInstances() throws RemoteException {
		// Setup cluster
		Node node1 = ClusterFactory.getBasicFactory().startCluster(1819, "test-cluster");
		LogNode logNode1 = LogNode.connectToCluster(1820, node1.getState().getAddress());
		LogNode logNode2 = LogNode.connectToCluster(1821, node1.getState().getAddress());
		assertEquals("test-cluster", logNode1.getState().getClusterId());
		// Use stub logger
		logNode1.setLogger(message -> lastLogMessage1 = message);
		logNode2.setLogger(message -> lastLogMessage2 = message);
		// Log and check
		Message message = new LogMessage("Test message", null);
		new ConnectorImpl(node1.getState()).log(message);
		assertNotNull(lastLogMessage1);
		assertNotNull(lastLogMessage2);
		assertEquals(lastLogMessage2, lastLogMessage1);
		// Shutdown
		node1.disconnect();
		logNode2.disconnect();
		logNode1.disconnect();
	}
}
