package nl.erwinvaneyk.communication.rmi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import lombok.NonNull;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.communication.MessageDistributor;
import nl.erwinvaneyk.communication.Socket;
import nl.erwinvaneyk.communication.exceptions.CommunicationException;
import nl.erwinvaneyk.core.NodeAddress;

public class RMISocket implements Socket {

	private final NodeAddress nodeAddress;
	private final RMIRegistry connection;

	public RMISocket(@NonNull NodeAddress nodeAddress) {
		this.nodeAddress = nodeAddress;
		try {
			this.connection = RMIRegistry.getRemote(nodeAddress.getLocation());
		}
		catch (RemoteException e) {
			throw new RuntimeException("Failed to open socket to " + nodeAddress, e);
		}
	}

	@Override
	public NodeAddress getConnectedNode() {
		return nodeAddress;
	}

	@Override
	public void sendMessage(@NonNull Message message) throws CommunicationException {
		try {
			MessageDistributor messageDistributor = connection.get(nodeAddress);
			messageDistributor.onMessageReceived(message);
		}
		catch (RemoteException | NotBoundException e) {
			throw new CommunicationException("Failed to send asynchronous message: '" + message + "' to " + nodeAddress, e);
		}
	}

	@Override
	public Message sendRequest(@NonNull Message message) throws CommunicationException {
		try {
			MessageDistributor messageDistributor = connection.get(nodeAddress);
			return messageDistributor.onRequestReceived(message);
		}
		catch (RemoteException | NotBoundException e) {
			throw new CommunicationException("Failed to send synchronous message: '" + message + "' to " + nodeAddress, e);
		}
	}
}
