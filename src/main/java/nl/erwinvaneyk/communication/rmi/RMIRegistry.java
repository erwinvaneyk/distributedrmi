package nl.erwinvaneyk.communication.rmi;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.erwinvaneyk.communication.MessageDistributor;
import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.exceptions.PortAlreadyInUseException;
import nl.erwinvaneyk.core.Address;
import nl.erwinvaneyk.core.NodeAddress;

@Slf4j
@NoArgsConstructor
public class RMIRegistry implements Server {

	private Registry registry;

	public static RMIRegistry getRemote(Address address) throws RemoteException {
		return new RMIRegistry(LocateRegistry.getRegistry(address.getIp(), address.getPort()));
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
				registry = LocateRegistry.createRegistry(port);
			}
			catch (InterruptedException | RemoteException e1) {
				throw new PortAlreadyInUseException("Failed to start RMI-registry (server) on port: " + port, e);
			}
		}
		return this;
	}

	@Override
	public Server start() {
		return this;
	}

	@Override
	public Server register(NodeAddress nodeAddress, MessageDistributor handler) {
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
	public MessageDistributor get(NodeAddress nodeAddress) throws RemoteException, NotBoundException {
		return (MessageDistributor) registry.lookup(nodeAddress.getIdentifier());
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
		catch (ConnectException e1) {
			log.debug("Could not shutdown registry: " + registry + ", it is already shutdown");
		} catch (RemoteException e) {
			log.debug("Could not shutdown port: " + e.getMessage());
		}
	}
}
