package nl.erwinvaneyk.core.logging;

import lombok.Getter;
import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.core.NodeAddress;

public class LogMessage extends BasicMessage{

	@Getter
	private final String log;

	public LogMessage(String log, NodeAddress origin) {
		super(LogNode.CONTEXT, origin);
		this.log = log;
	}

	@Override
	public String toString() {
		return (getOrigin() != null ? getOrigin() + ": " : "") + log;
	}
}
