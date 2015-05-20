package nl.erwinvaneyk.communication;

import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.core.ClusterFactory;
import nl.erwinvaneyk.core.Node;
import org.junit.Before;
import org.junit.Test;

import java.rmi.RemoteException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

// TODO: needs cluster-joins to be able to test
public class ConnectorImplTest {
    private boolean receivedOwnMessage = false;

    @Before
    public void reset() {
        receivedOwnMessage = false;
    }

    @Test
    public void connectorBroadcastSingleProcessNoFilter() throws RemoteException {
        // Setup cluster
        Node node1 = ClusterFactory.getBasicFactory().startCluster(1819, "test-cluster");
        node1.getMessageHandler().bind(new MessageReceivedHandler() {
            @Override
            public Message onMessage(Message message) throws CommunicationException {
                ConnectorImplTest.this.receivedOwnMessage = true;
                return null;
            }

            @Override
            public String getContext() {
                return "test-cluster";
            }
        });

        ConnectorImpl connector = (ConnectorImpl) node1.getConnector();

        Message message = new BasicMessage("test-cluster", node1.getState().getAddress());
        connector.broadcast(message);

        assertTrue(receivedOwnMessage);

        // Shutdown
        node1.disconnect();
    }


    @Test
    public void connectorBroadcastSingleProcessWithFilter() throws RemoteException {
        // Setup cluster
        Node node1 = ClusterFactory.getBasicFactory().startCluster(1820, "test-cluster");
        node1.getMessageHandler().bind(new MessageReceivedHandler() {
            @Override
            public Message onMessage(Message message) throws CommunicationException {
                ConnectorImplTest.this.receivedOwnMessage = true;
                return null;
            }

            @Override
            public String getContext() {
                return "test-cluster";
            }
        });

        ConnectorImpl connector = (ConnectorImpl) node1.getConnector();

        Message message = new BasicMessage("test-cluster", node1.getState().getAddress());
        connector.broadcast(message, "randomId");

        assertFalse(receivedOwnMessage);

        // Shutdown
        node1.disconnect();
    }
}
