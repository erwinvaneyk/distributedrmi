package nl.erwinvaneyk.communication.rmi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.UnknownHostException;
import java.rmi.RemoteException;

import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.Socket;
import nl.erwinvaneyk.core.ClusterFactory;
import nl.erwinvaneyk.core.Node;
import org.junit.Test;

public class RMISocketTest {

	@Test
	public void sendAsyncMessage() throws RemoteException {
		Node node1 = ClusterFactory.getBasicFactory().startCluster(1818, "test-cluster");
		TestMessageHandler handler = new TestMessageHandler();
		node1.getMessageDistributor().bind(handler);

		Message message = new BasicMessage(TestMessageHandler.CONTEXT, null);
		Socket socket = new RMISocket(node1.getState().getAddress());
		socket.sendMessage(message);

		assertEquals(handler.getLastMessage(), message);
		assertEquals(node1.getState().getAddress(), socket.getConnectedNode());
		node1.disconnect();
	}

	@Test
	public void sendSyncMessage() throws RemoteException, UnknownHostException {
		Node node1 = ClusterFactory.getBasicFactory().startCluster(1818, "test-cluster");
		node1.getMessageDistributor().bind(new TestMessageHandler());

		Message message = new BasicMessage(TestMessageHandler.CONTEXT, null);
		Socket socket = new RMISocket(node1.getState().getAddress());
		Message response = socket.sendRequest(message);

		assertTrue((Boolean) response.get("success"));
		node1.disconnect();
	}
}
