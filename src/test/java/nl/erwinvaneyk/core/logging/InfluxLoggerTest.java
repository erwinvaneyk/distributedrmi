package nl.erwinvaneyk.core.logging;

import static org.junit.Assume.assumeTrue;

import java.util.HashMap;
import java.util.Map;

import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.communication.Message;
import nl.erwinvaneyk.core.Address;
import nl.erwinvaneyk.core.NodeAddressImpl;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class InfluxLoggerTest {

	private static InfluxLogger influx;

	@Before
	public void beforeMethod() {
		assumeTrue(influx.isAvailable());
	}

	@BeforeClass
	public static void setupDatabaseConnection() {
		influx = new InfluxLogger(new Address("localhost",8086), "root", "root", "distributedrmi-test");
	}

	@AfterClass
	public static void removeTestDatabase() {
		influx.deleteDatabase();
	}

	@Test
	public void testLogEmpty() {
		Map<String, Object> args = new HashMap<>();
		influx.log("test", args);
	}


	@Test
	public void testLogWithValues() {
		Map<String, Object> args = new HashMap<>();
		args.put("abc", "def");
		args.put("value", 42);
		influx.log("test", args);
	}

	@Test
	public void testLogMessage() {
		Message message = new BasicMessage("test", new NodeAddressImpl("TEST-NODE", 1, new Address("127.0.0.1", 1818)));
		message.put("test-field", 42);
		influx.log(message);
	}
}
