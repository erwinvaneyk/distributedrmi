package nl.erwinvaneyk.core.logging;

import ch.qos.logback.classic.Level;
import lombok.Getter;
import nl.erwinvaneyk.communication.BasicMessage;
import nl.erwinvaneyk.core.NodeAddress;

public class LogMessage extends BasicMessage{

	@Getter
	private final String log;

	@Getter
	private final Level level;

	public LogMessage(String log, NodeAddress origin) {
		super(LogNode.CONTEXT, origin);
		this.log = log;
		this.level = Level.DEBUG;
	}

	public LogMessage(String log, Level level, NodeAddress origin) {
		super(LogNode.CONTEXT, origin);
		this.log = log;
		this.level = level;
	}

	@Override
	public String toString() {
		return (getOrigin() != null ? getOrigin() + ": " : "") + log;
	}
}
