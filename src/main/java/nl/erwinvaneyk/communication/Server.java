package nl.erwinvaneyk.communication;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import nl.erwinvaneyk.communication.exceptions.PortAlreadyInUseException;
import nl.erwinvaneyk.core.NodeAddress;

public interface Server {

	public static final int PORT_RANGE_MIN = 4242;

	public static final int PORT_RANGE_MAX = 5000;

	Server start(int port) throws PortAlreadyInUseException;

	Server start();

	Server register(NodeAddress nodeAddress, MessageHandler handler);

	Server unregister(NodeAddress nodeAddress);

	MessageHandler get(NodeAddress nodeAddress) throws RemoteException, NotBoundException;

	void shutdown();
}
