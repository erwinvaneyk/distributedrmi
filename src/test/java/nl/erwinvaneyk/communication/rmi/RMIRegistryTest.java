package nl.erwinvaneyk.communication.rmi;

import static junit.framework.TestCase.fail;
import static org.mockito.Mockito.mock;

import nl.erwinvaneyk.communication.MessageDistributor;
import nl.erwinvaneyk.communication.Server;
import nl.erwinvaneyk.communication.exceptions.PortAlreadyInUseException;
import nl.erwinvaneyk.core.NodeAddress;
import nl.erwinvaneyk.core.NodeAddressImpl;
import org.junit.Test;

public class RMIRegistryTest {

	@Test
	public void registerHandler() throws PortAlreadyInUseException {
		MessageDistributor messageDistributorStub = mock(MessageDistributor.class);
		Server reg = new RMIRegistry()
				.start(1818)
				.register(new NodeAddressImpl("test", 0, null), messageDistributorStub);
		// todo: test
		reg.shutdown();
	}

	@Test
	public void unregisterHandler() throws PortAlreadyInUseException {
		MessageDistributor messageDistributorStub = mock(MessageDistributor.class);
		NodeAddress mockAddress = new NodeAddressImpl("test", 0, null);
		Server reg = new RMIRegistry()
				.start(1818)
				.register(mockAddress, messageDistributorStub)
				.unregister(mockAddress)
				.register(mockAddress, messageDistributorStub);
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

	@Test
	public void startingRegistryOnUnavailablePort() throws PortAlreadyInUseException {
		Server registry = new RMIRegistry().start(1818);
		try {
			new RMIRegistry().start(1818);
			fail();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			registry.shutdown();
		}
	}
}
