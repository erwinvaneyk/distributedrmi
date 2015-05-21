package nl.erwinvaneyk.core.logging;

import java.util.Map;

import nl.erwinvaneyk.communication.Message;

public interface RawLogger extends Logger {

	void log(String subject, Map<String,Object> args);

	void log(String subject, Message message);

	public static RawLogger getDefault() {
		return InfluxLogger.getInstance();
	}
}
