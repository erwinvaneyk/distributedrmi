package nl.erwinvaneyk.communication.rmi;

import static org.mockito.Mockito.mock;

import nl.erwinvaneyk.communication.MessageHandler;
import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.exceptions.PortAlreadyInUseException;
import nl.erwinvaneyk.core.NodeAddress;
import nl.erwinvaneyk.core.NodeAddressImpl;
import org.junit.Test;

public class RMIRegistryTest {

	@Test
	public void registerHandler() throws PortAlreadyInUseException {
		MessageHandler messageHandlerStub = mock(MessageHandler.class);
		Server reg = new RMIRegistry()
				.start(1818)
				.register(new NodeAddressImpl("test", 0, null), messageHandlerStub);
		// todo: test
		reg.shutdown();
	}

	@Test
	public void unregisterHandler() throws PortAlreadyInUseException {
		MessageHandler messageHandlerStub = mock(MessageHandler.class);
		NodeAddress mockAddress = new NodeAddressImpl("test", 0, null);
		Server reg = new RMIRegistry()
				.start(1818)
				.register(mockAddress, messageHandlerStub)
				.unregister(mockAddress)
				.register(mockAddress, messageHandlerStub);
		reg.shutdown();
	}

	@Test
	public void stopRegistry() throws PortAlreadyInUseException {
		new RMIRegistry()
				.start(1818)
				.shutdown();
		// Check if port has been released
		new RMIRegistry()
				.start(1818)
				.shutdown();
	}
}