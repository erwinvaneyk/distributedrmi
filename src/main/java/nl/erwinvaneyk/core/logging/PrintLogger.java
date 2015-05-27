package nl.erwinvaneyk.core.logging;

import nl.erwinvaneyk.communication.Message;

public class PrintLogger implements Logger {

	@Override
	public void log(Message message) {
		if(message instanceof LogMessage) {
			LogMessage logMessage = (LogMessage) message;
			if(logMessage.getLog() == null || "null".equals(logMessage.getLog())) {
				return;
			}
		}

		System.out.println(message.toString());
	}

	@Override
	public boolean isAvailable() {
		return true;
	}
}
