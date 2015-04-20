package nl.erwinvaneyk.core.logging;

import nl.erwinvaneyk.communication.Message;

public class PrintLogger implements Logger {

	@Override
	public void log(Message message) {
		System.out.println(message.toString());
	}
}
