package nl.erwinvaneyk.communication.rmi;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;

import lombok.NoArgsConstructor;
import nl.erwinvaneyk.communication.MessageHandler;
import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.exceptions.PortAlreadyInUseException;
import nl.erwinvaneyk.core.Address;
import nl.erwinvaneyk.core.NodeAddress;

@NoArgsConstructor
public class RMIRegistry implements Server {

	private Registry registry;

	public static RMIRegistry getRemote(Address address) throws RemoteException {
		return new RMIRegistry(LocateRegistry.getRegistry(address.getIp(), address.getPort()));
	}

	public static void cleanup(int port) {
		try {
			new RMIRegistry(LocateRegistry.getRegistry(String.valueOf(Inet4Address.getLocalHost().getHostAddress()), port)).shutdown();
		}
		catch (RemoteException | UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public RMIRegistry(Registry registry) {
		this.registry = registry;
	}

	@Override
	public Server start(int port) throws PortAlreadyInUseException{
		try {
			registry = LocateRegistry.createRegistry(port);
		}
		catch (RemoteException e) {
			try {
				// Fix race condition, when previous registry is still in shutdown.
				Thread.sleep(500);
				return start(port);
			}
			catch (InterruptedException e1) {
				throw new PortAlreadyInUseException("Failed to start RMI-registry (server).", e);
			}
		}
		return this;
	}

	@Override
	public Server start() {
		return this;
	}

	@Override
	public Server register(NodeAddress nodeAddress, MessageHandler handler) {
		try {
			registry.bind(nodeAddress.getIdentifier(), handler);
		}
		catch (RemoteException | AlreadyBoundException e) {
			throw new RuntimeException(e);
		}
		return this;
	}

	@Override
	public Server unregister(NodeAddress nodeAddress) {
		try {
			registry.unbind(nodeAddress.getIdentifier());
		}
		catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MessageHandler get(NodeAddress nodeAddress) throws RemoteException, NotBoundException {
		return (MessageHandler) registry.lookup(nodeAddress.getIdentifier());
	}

	@Override
	public void shutdown() {
		try {
			for(String binding : registry.list()) {
				try {
					registry.unbind(binding);
				}
				catch (RemoteException | NotBoundException e) {
					e.printStackTrace();
				}
			}
			UnicastRemoteObject.unexportObject(registry, true);
		}
		catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}